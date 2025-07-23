package com.pro.salehero.users.article.service

import com.pro.salehero.admins.article.controller.dto.ArticleDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.users.article.domain.Article
import com.pro.salehero.users.article.domain.ArticleRepository
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
    ): PageResponseDTO<ArticleDTO> {
        return userArticleRepository.getArticles(pageable, isAdmin = false)
    }

    @Transactional(readOnly = true)
    fun getArticleDetail(
        id: Long,
        request: HttpServletRequest
    ): ArticleDTO {
        val article = getArticle(id)
        viewCountService.increaseViewCount(
            RedisContentType.ARTICLE,
            id,
            comfortUtil.getUserIdentifier(request)
        )

        return ArticleDTO.of(article)
    }

    private fun getArticle(
        id: Long
    ): Article {
        return userArticleRepository.findById(id)
            .orElseThrow { CustomException(ErrorCode.CODE_404) }
    }
}
