package com.pro.salehero.admins.article.service

import com.pro.salehero.domain.article.dto.AdminArticleDTO
import com.pro.salehero.domain.article.dto.AdminArticleDeleteDTO
import com.pro.salehero.domain.article.dto.AdminArticlePostDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.service.dto.ViewCount
import com.pro.salehero.domain.article.Article
import com.pro.salehero.domain.article.ArticleRepository
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
    ): PageResponseDTO<AdminArticleDTO> = articleRepository.getAdminArticles(pageable)

    @Transactional
    fun modifyAdminArticle(
        articleId: Long,
        dto: AdminArticlePostDTO
    ): AdminArticleDTO {
        val article = existsArticle(articleId)
        article.update(
            title = dto.title,
            content = dto.content,
            category = dto.category,
            summary = dto.summary,
            isVisible = dto.isVisible,
        )

        return AdminArticleDTO.of(article)
    }

    @Transactional
    fun deleteArticle(
        dto: AdminArticleDeleteDTO,
    ) = articleRepository.findAllById(dto.idxList)
        .onEach { it.isDeleted = "Y" }

    @Transactional
    fun updateViewCount(
        viewCount: ViewCount
    ) = articleRepository.updateViewCount(viewCount)

    private fun existsArticle(
        articleId: Long
    ): Article = articleRepository.findById(articleId)
        .orElseThrow { CustomException(ErrorCode.CODE_404) }
}
