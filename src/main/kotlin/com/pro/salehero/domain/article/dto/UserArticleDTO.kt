package com.pro.salehero.domain.article.dto

import com.pro.salehero.domain.article.Article
import com.pro.salehero.common.enums.ContentsCategory
import java.time.LocalDateTime

data class UserArticleDTO (
    val id: Long,
    val title: String,
    val content: String,
    val summary: String? = null,
    val category: ContentsCategory,
    val viewCount: Long,
    val createdAt: LocalDateTime,
){
    companion object {
        fun of(article: Article): UserArticleDTO {
            return UserArticleDTO(
                id = article.id!!,
                title = article.title,
                content = article.content,
                summary = article.summary,
                category = article.category,
                viewCount = article.viewCount,
                createdAt = article.createdAt,
            )
        }
    }
}
