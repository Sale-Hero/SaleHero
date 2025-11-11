package com.pro.salehero.chat.controller

import com.pro.salehero.chat.service.ChatHistoryService
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.chat.dto.ChatMessageDto
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat")
class ChatHistoryController(
    private val chatHistoryService: ChatHistoryService
) {

    @GetMapping("/history")
    fun getChatHistory(
        @PageableDefault(size = 50, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): PageResponseDTO<ChatMessageDto> {
        return chatHistoryService.getChatHistory(pageable)
    }
}
