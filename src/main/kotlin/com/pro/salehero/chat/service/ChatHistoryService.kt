package com.pro.salehero.chat.service

import com.pro.salehero.chat.dto.ChatMessageDto
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.domain.chat.ChatMessageRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatHistoryService(
    private val chatMessageRepository: ChatMessageRepository
) {

    @Transactional(readOnly = true)
    fun getChatHistory(pageable: Pageable): PageResponseDTO<ChatMessageDto> {
        val page = chatMessageRepository.findAll(pageable)
        val dtoList = page.content.map {
            ChatMessageDto(
                type = it.type,
                sender = it.sender,
                content = it.content,
                createdAt = it.createdAt
            )
        }
        return PageResponseDTO(
            totalPages = page.totalPages,
            totalElement = page.totalElements,
            content = dtoList
        )
    }
}
