package com.pro.salehero.users.newsletter.controller.dto

import com.pro.salehero.users.newsletter.domain.NewsLetter
import java.time.LocalDateTime

data class NewsLetterResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val isSent: String,
    val isPublic: String,
    val sentAt: LocalDateTime?,
    val createdAt: LocalDateTime?
){
    companion object {
        fun of(newsLetter: NewsLetter): NewsLetterResponseDTO {
            return NewsLetterResponseDTO(
                id = newsLetter.id!!,
                title = newsLetter.title,
                content = newsLetter.content,
                isSent = "",
                isPublic = "",
                sentAt = null,
                createdAt = newsLetter.createdAt,
            )
        }
    }
}
