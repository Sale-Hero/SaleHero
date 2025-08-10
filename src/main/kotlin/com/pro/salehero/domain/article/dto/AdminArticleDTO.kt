package com.pro.salehero.domain.article.dto

import com.pro.salehero.domain.article.Article
import com.pro.salehero.domain.community.enums.ContentsCategory
import java.time.LocalDateTime

data class AdminArticleDTO (
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
        fun of(article: Article): AdminArticleDTO {
            return AdminArticleDTO(
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
