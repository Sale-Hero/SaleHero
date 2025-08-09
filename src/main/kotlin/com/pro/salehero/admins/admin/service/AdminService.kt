package com.pro.salehero.admins.admin.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPostDTO
import com.pro.salehero.domain.newsletter.NewsLetter
import com.pro.salehero.domain.newsletter.NewsLetterRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val newsLetterRepository: NewsLetterRepository,
    private val objectMapper: ObjectMapper
){
    fun createNewsLetter(
        dto: NewsLetterPostDTO
    ) = apply { newsLetterRepository.save(
        NewsLetter(
            title = dto.title,
            content = objectMapper.writeValueAsString(dto.content),
            isSent = "N",
            isPublic = "N",
            sentAt = dto.sentAt,
            viewCount = 0
        )
    ) }
}