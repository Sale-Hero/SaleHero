package com.pro.salehero.admins.article.service

import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.users.article.domain.Article
import com.pro.salehero.users.article.domain.ArticleRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminArticleService(
    private val articleRepository: ArticleRepository
) {

    @Transactional
    fun createArticle(
        dto: AdminArticlePostDTO
    ) = Article(
        title = dto.title,
        content = dto.content,
        category = dto.category,
        summary = dto.summary,
    )
        .also { articleRepository.save(it) }
        .also { ResponseEntity.ok() }
}
