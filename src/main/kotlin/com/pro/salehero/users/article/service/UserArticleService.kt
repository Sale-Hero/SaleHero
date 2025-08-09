package com.pro.salehero.users.article.service

import com.pro.salehero.admins.article.controller.dto.AdminArticleDTO
import com.pro.salehero.admins.article.controller.dto.UserArticleDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.domain.article.Article
import com.pro.salehero.domain.article.ArticleRepository
import com.pro.salehero.util.comfortutil.ComfortUtil
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserArticleService(
    private val userArticleRepository: ArticleRepository,
    private val viewCountService: ViewCountService,
    private val comfortUtil: ComfortUtil
) {

    @Transactional(readOnly = true)
    fun getUserArticles(
        pageable: Pageable
    ): PageResponseDTO<UserArticleDTO> {
        return userArticleRepository.getUserArticles(pageable)
    }

    @Transactional(readOnly = true)
    fun getArticleDetail(
        id: Long,
        request: HttpServletRequest
    ): UserArticleDTO {
        val article = getArticle(id)
        increaseViewCount(id, request)

        return UserArticleDTO.of(article)
    }

    private fun getArticle(
        id: Long
    ): Article {
        return userArticleRepository.findById(id)
            .orElseThrow { CustomException(ErrorCode.CODE_404) }
    }

    private fun increaseViewCount(
        id: Long,
        request: HttpServletRequest
    ) = viewCountService.increaseViewCount(
        RedisContentType.ARTICLE,
        id,
        comfortUtil.getUserIdentifier(request)
    )
}
