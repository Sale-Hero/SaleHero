package com.pro.salehero.admins.article.service

import com.pro.salehero.admins.article.controller.dto.AdminArticleDTO
import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.users.article.domain.Article
import com.pro.salehero.users.article.domain.ArticleRepository
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
        )
    )

    fun getAdminArticles(
        pageable: Pageable
    ): PageResponseDTO<AdminArticleDTO> {
        return articleRepository.getArticles(pageable)
    }
}
