package com.pro.salehero.admins.article.controller.dto

import com.pro.salehero.users.article.domain.Article
import com.pro.salehero.users.community.domain.enums.ArticleCategory

data class AdminArticleDTO (
    val id: Long,
    val title: String,
    val content: String,
    val summary: String? = null,
    val category: ArticleCategory,
    val viewCount: Long,
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
                isVisible = article.isVisible ?: "N",
                isDeleted = article.isDeleted ?: "N"
            )
        }
    }
}
