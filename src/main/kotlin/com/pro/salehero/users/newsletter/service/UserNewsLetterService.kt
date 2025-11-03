package com.pro.salehero.users.newsletter.service

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.users.newsletter.dto.NewsLetterResponseDTO
import com.pro.salehero.domain.newsletter.NewsLetterRepository
import com.pro.salehero.util.comfortutil.ComfortUtil
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserNewsLetterService(
    private val newsLetterRepository: NewsLetterRepository,
    private val comfortUtil: ComfortUtil,
    private val viewCountService: ViewCountService
) {
    @Transactional(readOnly = true)
    fun getNewsLetters(
        pageable: Pageable,
        query: String?,
    ) = newsLetterRepository.searchUserNewsLetter(pageable, query)

    @Transactional(readOnly = true)
    fun getNewsLetter(
        id: Long,
        request: HttpServletRequest
    ): NewsLetterResponseDTO = newsLetterRepository.findById(id)
        .orElseThrow { CustomException(ErrorCode.CODE_404) }
        .apply { viewCountService.increaseViewCount(RedisContentType.NEWS_LETTER, id, comfortUtil.getUserIdentifier(request)) }
        .let { NewsLetterResponseDTO.of(newsLetter = it) }

    private fun increaseViewCount() {

    }
}