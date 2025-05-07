package com.pro.salehero.newsletter.service

import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterDeleteDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPostDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterResponseDTO
import com.pro.salehero.newsletter.domain.NewsLetter
import com.pro.salehero.newsletter.domain.NewsLetterRepository
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserNewsLetterService(
    val newsLetterRepository: NewsLetterRepository
) {
    @Transactional(readOnly = true)
    fun getNewsLetters(
        pageable: Pageable,
        query: String?,
    ) = newsLetterRepository.searchUserNewsLetter(pageable, query)

    @Transactional(readOnly = true)
    fun getNewsLetter(
        id: Long
    ): NewsLetterResponseDTO = newsLetterRepository.findById(id)
        .orElseThrow { CustomException(ErrorCode.CODE_404) }
        .let { NewsLetterResponseDTO.of(newsLetter = it) }
}