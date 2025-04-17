package com.pro.salehero.admin.service

import com.pro.salehero.newsletter.controller.dto.NewsLetterPostDTO
import com.pro.salehero.newsletter.domain.NewsLetter
import com.pro.salehero.newsletter.domain.NewsLetterRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val newsLetterRepository: NewsLetterRepository
){
    fun createNewsLetter(
        dto: NewsLetterPostDTO
    ) = apply { newsLetterRepository.save(
        NewsLetter(
            title = dto.title,
            content = dto.content,
            isSent = "N",
            sentAt = dto.sentAt
        )
    ) }
}