package com.pro.salehero.newsletter.domain

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterResponseDTO
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface NewsLetterRepositoryCustom {
    fun searchNewsLetter(pageable: Pageable, query: String?): PageResponseDTO<NewsLetterResponseDTO>?

    fun updateNewsLetter(dto: NewsLetterPutDTO):Long

    fun remove(idx: Long): Long

    // 오늘 보낼 뉴스레터 조회
    fun findTodayNewsLetter(today: LocalDateTime): List<NewsLetterResponseDTO>?

    // 보냄 처리
    fun updateToSent(dto: NewsLetterResponseDTO)
}