package com.pro.salehero.admins.article.service

import com.pro.salehero.admins.article.controller.dto.AdminArticleDTO
import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.users.article.domain.Article
import com.pro.salehero.users.article.domain.ArticleRepository
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminArticleService(
    private val articleRepository: ArticleRepository
) {

    @Transactional
    fun createAdminArticle(
        dto: AdminArticlePostDTO
    ) = articleRepository.save(
        Article(
            title = dto.title,
            content = dto.content,
            category = dto.category,
            summary = dto.summary,
            isVisible = dto.isVisible,
        )
    )

    fun getAdminArticles(
        pageable: Pageable
    ): PageResponseDTO<AdminArticleDTO> {
        return articleRepository.getArticles(pageable)
    }

    @Transactional
    fun modifyAdminArticle(
        articleId: Long,
        dto: AdminArticlePostDTO
    ): AdminArticleDTO {
        val article = existsArticle(articleId)
        article.update(dto)

        return AdminArticleDTO.of(article)
    }

    @Transactional
    fun deleteArticle(
        articleId: Long
    ) = existsArticle(articleId)
        .apply { isDeleted = "Y" }

    private fun existsArticle(
        articleId: Long
    ): Article = articleRepository.findById(articleId)
        .orElseThrow { CustomException(ErrorCode.CODE_404) }
}
