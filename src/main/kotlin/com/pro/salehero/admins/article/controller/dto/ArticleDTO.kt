package com.pro.salehero.admins.article.controller.dto

import com.pro.salehero.users.article.domain.Article
import com.pro.salehero.users.community.domain.enums.ContentsCategory
import java.time.LocalDateTime

data class ArticleDTO (
    val id: Long,
    val title: String,
    val content: String,
    val summary: String? = null,
    val category: ContentsCategory,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val isVisible: String,
    val isDeleted: String,
){
    companion object {
        fun of(article: Article): ArticleDTO {
            return ArticleDTO(
                id = article.id!!,
                title = article.title,
                content = article.content,
                summary = article.summary,
                category = article.category,
                viewCount = article.viewCount,
                createdAt = article.createdAt,
                isVisible = article.isVisible ?: "N",
                isDeleted = article.isDeleted ?: "N"
            )
        }
    }
}
