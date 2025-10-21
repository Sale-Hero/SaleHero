package com.pro.salehero.chat.controller

import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.concurrent.atomic.AtomicInteger
import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.chat.dto.ChatMessageDto // Changed import
import com.pro.salehero.domain.chat.MessageType

@Controller
class ChatController(
    private val messagingTemplate: SimpMessageSendingOperations,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val userCount = AtomicInteger(0)
    private val CHAT_MESSAGE_KEY = "chat:messages" // Redis List/Stream Key

    @MessageMapping("/chat.sendMessage")
    fun sendMessage(@Payload chatMessage: ChatMessageDto) { // Changed type
        // 메시지를 Redis에 저장 (JSON 형태로)
        val messageJson = objectMapper.writeValueAsString(chatMessage)
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

        // 메시지 브로드캐스팅
        messagingTemplate.convertAndSend("/topic/chat", chatMessage)
    }

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = headerAccessor.sessionId ?: return
        val anonymousUser = "익명 ${userCount.incrementAndGet()}"

        headerAccessor.sessionAttributes?.put("username", anonymousUser)

        val chatMessage = ChatMessageDto(MessageType.JOIN, anonymousUser, "$anonymousUser 님이 입장했습니다.") // Changed type
        // 입장 메시지를 Redis에 저장
        val messageJson = objectMapper.writeValueAsString(chatMessage)
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

        messagingTemplate.convertAndSend("/topic/chat", chatMessage)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as? String

        if (username != null) {
            val chatMessage = ChatMessageDto(MessageType.LEAVE, username, "$username 님이 퇴장했습니다.") // Changed type
            // 퇴장 메시지를 Redis에 저장
            val messageJson = objectMapper.writeValueAsString(chatMessage)
            redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, messageJson)

            messagingTemplate.convertAndSend("/topic/chat", chatMessage)
        }
    }
}
