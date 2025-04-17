package com.pro.salehero.newsletter.domain

import com.pro.salehero.common.PageResponseDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterResponseDTO
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface NewsLetterRepositoryCustom {
    fun searchNewsLetter(pageable: Pageable, query: String?): PageResponseDTO<NewsLetterResponseDTO>?

    fun updateNewsLetter(dto: NewsLetterPutDTO):Long

    fun remove(idx: Long): Long

    fun findTodayNewsLetter(today: LocalDateTime): List<NewsLetterResponseDTO>?
}