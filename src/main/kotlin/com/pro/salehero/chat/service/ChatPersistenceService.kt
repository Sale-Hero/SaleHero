package com.pro.salehero.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.chat.dto.ChatMessageDto
import com.pro.salehero.domain.chat.ChatMessage
import com.pro.salehero.domain.chat.ChatMessageRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class ChatPersistenceService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val chatMessageRepository: ChatMessageRepository,
    private val objectMapper: ObjectMapper
) {

    private val CHAT_MESSAGE_KEY = "chat:messages"
    private val BATCH_SIZE = 100L

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    @Transactional
    fun persistChatMessages() {
        val messageJsonList = redisTemplate.opsForList().range(CHAT_MESSAGE_KEY, 0, BATCH_SIZE - 1)

        if (messageJsonList.isNullOrEmpty()) {
            return
        }

        val messagesToPersist = messageJsonList.mapNotNull { messageJson ->
            try {
                val chatMessageDto = objectMapper.readValue(messageJson, ChatMessageDto::class.java)
                ChatMessage(
                    type = chatMessageDto.type,
                    sender = chatMessageDto.sender ?: "anonymous",
                    content = chatMessageDto.content
                ).apply {
                    chatMessageDto.createdAt?.let { this.createdAt = it }
                }
            } catch (e: Exception) {
                println("Error parsing chat message from Redis: $messageJson, Error: ${e.message}")
                null
            }
        }

        if (messagesToPersist.isNotEmpty()) {
            chatMessageRepository.saveAll(messagesToPersist)
            redisTemplate.opsForList().trim(CHAT_MESSAGE_KEY, messageJsonList.size.toLong(), -1)
            println("Persisted ${messagesToPersist.size} chat messages to MySQL.")
        }
    }
}
