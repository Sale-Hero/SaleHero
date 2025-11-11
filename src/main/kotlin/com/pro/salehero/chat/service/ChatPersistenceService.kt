package com.pro.salehero.chat.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.domain.chat.ChatMessageRepository
import com.pro.salehero.domain.chat.ChatMessage // Entity
import com.pro.salehero.chat.dto.ChatMessageDto // DTO - Changed import and removed alias
import java.util.concurrent.TimeUnit

@Service
class ChatPersistenceService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val chatMessageRepository: ChatMessageRepository,
    private val objectMapper: ObjectMapper
) {

    private val CHAT_MESSAGE_KEY = "chat:messages" // Redis List/Stream Key
    private val BATCH_SIZE = 100 // 한 번에 처리할 메시지 수

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES) // 5초마다 실행
    @Transactional
    fun persistChatMessages() {
        val messagesToPersist = mutableListOf<ChatMessage>() // Entity
        var count = 0

        // Redis에서 메시지를 배치로 가져옴
        while (count < BATCH_SIZE) {
            val messageJson = redisTemplate.opsForList().leftPop(CHAT_MESSAGE_KEY) // 왼쪽에서 가져옴
            if (messageJson == null) {
                break // 더 이상 메시지가 없으면 종료
            }

            try {
                val chatMessageDto = objectMapper.readValue(messageJson, ChatMessageDto::class.java) // Use DTO
                val entity = ChatMessage( // Entity
                    type = chatMessageDto.type,
                    sender = chatMessageDto.sender ?: "anonymous",
                    content = chatMessageDto.content
                ).apply {
                    chatMessageDto.createdAt?.let { this.createdAt = it }
                }
                messagesToPersist.add(entity)
                count++
            } catch (e: Exception) {
                // JSON 파싱 오류 처리 (로그 기록 등)
                println("Error parsing chat message from Redis: $messageJson, Error: ${e.message}")
            }
        }

        if (messagesToPersist.isNotEmpty()) {
            chatMessageRepository.saveAll(messagesToPersist) // 배치 저장
            println("Persisted ${messagesToPersist.size} chat messages to MySQL.")
        }
    }
}
