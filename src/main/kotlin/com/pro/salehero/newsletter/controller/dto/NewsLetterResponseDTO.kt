package com.pro.salehero.newsletter.controller.dto

import com.pro.salehero.newsletter.domain.NewsLetter
import java.time.LocalDateTime

data class NewsLetterResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val isSent: String,
    val sentAt: LocalDateTime?,
    val createdAt: LocalDateTime
){
    companion object {
        fun of(newsLetter: NewsLetter): NewsLetterResponseDTO {
            return NewsLetterResponseDTO(
                id = newsLetter.id!!,
                title = newsLetter.title,
                content = newsLetter.content,
                isSent = "",
                sentAt = null,
                createdAt = newsLetter.createdAt,
            )
        }
    }
}
