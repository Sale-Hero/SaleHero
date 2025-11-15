package com.pro.salehero.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.chat.dto.ChatMessageDto
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.domain.chat.ChatMessageRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatHistoryService(
    private val chatMessageRepository: ChatMessageRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) {

    private val CHAT_MESSAGE_KEY = "chat:messages"

    @Transactional(readOnly = true)
    fun getChatHistory(pageable: Pageable): PageResponseDTO<ChatMessageDto> {
        val page = chatMessageRepository.findAll(pageable)
        val dbChatMessageDtoList = page.content.map {
            ChatMessageDto(
                type = it.type,
                sender = it.sender,
                content = it.content,
                createdAt = it.createdAt
            )
        }

        var combinedList = dbChatMessageDtoList

        if (pageable.pageNumber == 0) {
            val redisMessagesJson = redisTemplate.opsForList().range(CHAT_MESSAGE_KEY, 0, -1) ?: emptyList()
            val redisChatMessageDtoList = redisMessagesJson.mapNotNull { json ->
                try {
                    objectMapper.readValue(json, ChatMessageDto::class.java)
                } catch (e: Exception) {
                    null // Handle parsing error
                }
            }
            combinedList = (redisChatMessageDtoList + dbChatMessageDtoList).distinctBy { it.createdAt.toString() + it.sender + it.content }
        }


        return PageResponseDTO(
            totalPages = page.totalPages,
            totalElement = page.totalElements,
            content = combinedList
        )
    }
}
