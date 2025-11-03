package com.pro.salehero.domain.newsletter

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.users.newsletter.dto.NewsLetterPutDTO
import com.pro.salehero.users.newsletter.dto.NewsLetterResponseDTO
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface NewsLetterRepositoryCustom {
    // 유저용 조회
    fun searchUserNewsLetter(pageable: Pageable, query: String?): PageResponseDTO<NewsLetterResponseDTO>?

    // 관리자용 전체 조회
    fun searchAdminNewsLetter(pageable: Pageable, query: String?): PageResponseDTO<NewsLetterResponseDTO>?

    fun updateNewsLetter(dto: NewsLetterPutDTO):Long

    fun remove(idx: Long): Long

    // 오늘 보낼 뉴스레터 조회
    fun findTodayNewsLetter(today: LocalDateTime): List<NewsLetterResponseDTO>?

    // 보냄 처리
    fun updateToSent(dto: NewsLetterResponseDTO)
}