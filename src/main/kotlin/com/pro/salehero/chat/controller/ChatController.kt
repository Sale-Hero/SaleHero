package com.pro.salehero.chat.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.chat.dto.ChatMessageDto
import com.pro.salehero.domain.chat.MessageType
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Controller
class ChatController(
    private val messagingTemplate: SimpMessageSendingOperations,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val log = LoggerFactory.getLogger(ChatController::class.java)
    private val userCount = AtomicInteger(0)
    private val sessionUserMap = ConcurrentHashMap<String, String>()
    private val CHAT_MESSAGE_KEY = "chat:messages" // Redis List/Stream Key

    @MessageMapping("/chat.sendMessage")
    fun sendMessage(@Payload chatMessage: ChatMessageDto, headerAccessor: SimpMessageHeaderAccessor) {
        val principalName = headerAccessor.user?.name ?: return
        val username = sessionUserMap[principalName]
        val sessionId = headerAccessor.sessionId ?: return
        val newChatMessage = chatMessage.copy(
            sender = username,
            createdAt = java.time.LocalDateTime.now(),
            sessionId = sessionId
        )
        // 메시지를 Redis에 저장 (JSON 형태로)
        val messageJson = objectMapper.writeValueAsString(newChatMessage)
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

        // 메시지 브로드캐스팅
        messagingTemplate.convertAndSend("/topic/chat", newChatMessage)
    }

    @MessageMapping("/chat.ready")
    fun handleReady(headerAccessor: StompHeaderAccessor) {
        val principalName = headerAccessor.user?.name ?: return
        val sessionId = headerAccessor.sessionId ?: return

        // Avoid re-processing if user is already in the map
        if (sessionUserMap.containsKey(principalName)) {
            return
        }

        val anonymousUser = "익명 ${userCount.incrementAndGet()}"
        sessionUserMap[principalName] = anonymousUser

        // Send private message to the user with their assigned anonymous name
        val identityMessage = ChatMessageDto(
            type = MessageType.SYSTEM,
            sender = anonymousUser,
            content = "Your assigned username is $anonymousUser",
            sessionId = sessionId
        )
        messagingTemplate.convertAndSendToUser(principalName, "/queue/private", identityMessage)
        log.info("Sent identity message to user: $principalName, username: $anonymousUser")

        val chatMessage = ChatMessageDto(MessageType.JOIN, anonymousUser, "$anonymousUser 님이 입장했습니다.", createdAt = java.time.LocalDateTime.now(), sessionId = sessionId)
        // 입장 메시지를 Redis에 저장
        val messageJson = objectMapper.writeValueAsString(chatMessage)
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

        messagingTemplate.convertAndSend("/topic/chat", chatMessage)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val principalName = headerAccessor.user?.name ?: return
        val username = sessionUserMap[principalName]
        val sessionId = headerAccessor.sessionId ?: return

        if (username != null) {
            val chatMessage = ChatMessageDto(MessageType.LEAVE, username, "$username 님이 퇴장했습니다.", createdAt = java.time.LocalDateTime.now(), sessionId = sessionId)
            // 퇴장 메시지를 Redis에 저장
            val messageJson = objectMapper.writeValueAsString(chatMessage)
            redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

            messagingTemplate.convertAndSend("/topic/chat", chatMessage)

            // Remove user from map
            sessionUserMap.remove(principalName)
        }
    }
}

