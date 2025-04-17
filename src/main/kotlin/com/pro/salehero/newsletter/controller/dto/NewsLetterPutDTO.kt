package com.pro.salehero.newsletter.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class NewsLetterPutDTO(
    val idx: Long,
    val title: String?,
    val content: String?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val sentAt: LocalDateTime,
)
