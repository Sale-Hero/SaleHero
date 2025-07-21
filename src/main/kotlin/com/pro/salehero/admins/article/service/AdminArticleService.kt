package com.pro.salehero.admins.article.service

import com.pro.salehero.admins.article.controller.dto.ArticleDTO
import com.pro.salehero.admins.article.controller.dto.AdminArticleDeleteDTO
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
    ): PageResponseDTO<ArticleDTO> = articleRepository.getArticles(pageable, isAdmin = true)

    @Transactional
    fun modifyAdminArticle(
        articleId: Long,
        dto: AdminArticlePostDTO
    ): ArticleDTO {
        val article = existsArticle(articleId)
        article.update(dto)

        return ArticleDTO.of(article)
    }

    @Transactional
    fun deleteArticle(
        dto: AdminArticleDeleteDTO,
    ) = articleRepository.findAllById(dto.idxList)
        .onEach { it.isDeleted = "Y" }

    private fun existsArticle(
        articleId: Long
    ): Article = articleRepository.findById(articleId)
        .orElseThrow { CustomException(ErrorCode.CODE_404) }
}
