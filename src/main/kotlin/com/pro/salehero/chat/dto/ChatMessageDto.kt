package com.pro.salehero.chat.dto

import com.pro.salehero.domain.chat.MessageType

data class ChatMessageDto(
    val type: MessageType,
    val sender: String,
    val content: String?
)