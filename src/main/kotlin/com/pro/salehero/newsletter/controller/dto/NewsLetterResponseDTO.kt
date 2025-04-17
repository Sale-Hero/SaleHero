package com.pro.salehero.newsletter.controller.dto

import java.time.LocalDateTime

data class NewsLetterResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val isSent: String,
    val sentAt: LocalDateTime,
    val createdAt: LocalDateTime
)
