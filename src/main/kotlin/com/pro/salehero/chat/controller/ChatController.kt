package com.pro.salehero.chat.controller

import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload

import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.concurrent.atomic.AtomicInteger
import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.chat.dto.ChatMessageDto // Changed import
import com.pro.salehero.domain.chat.MessageType

import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import java.util.concurrent.ConcurrentHashMap

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
    fun sendMessage(@Payload chatMessage: ChatMessageDto, headerAccessor: SimpMessageHeaderAccessor) { // Changed type
        val username = sessionUserMap[headerAccessor.sessionId]
        val newChatMessage = chatMessage.copy(
            sender = username,
            createdAt = java.time.LocalDateTime.now()
        )
        // 메시지를 Redis에 저장 (JSON 형태로)
        val messageJson = objectMapper.writeValueAsString(newChatMessage)
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

        // 메시지 브로드캐스팅
        messagingTemplate.convertAndSend("/topic/chat", newChatMessage)
    }

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = headerAccessor.sessionId ?: return
        val anonymousUser = "익명 ${userCount.incrementAndGet()}"

        sessionUserMap[sessionId] = anonymousUser

        val chatMessage = ChatMessageDto(MessageType.JOIN, anonymousUser, "$anonymousUser 님이 입장했습니다.", createdAt = java.time.LocalDateTime.now(), sessionId = sessionId) // Changed type
        // 입장 메시지를 Redis에 저장
        val messageJson = objectMapper.writeValueAsString(chatMessage)
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

        messagingTemplate.convertAndSend("/topic/chat", chatMessage)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = headerAccessor.sessionId ?: return
        val username = sessionUserMap[sessionId]

        if (username != null) {
            val chatMessage = ChatMessageDto(MessageType.LEAVE, username, "$username 님이 퇴장했습니다.", createdAt = java.time.LocalDateTime.now()) // Changed type
            // 퇴장 메시지를 Redis에 저장
            val messageJson = objectMapper.writeValueAsString(chatMessage)
            redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

            messagingTemplate.convertAndSend("/topic/chat", chatMessage)

            // Remove user from map
            sessionUserMap.remove(sessionId)
        }
    }
}
