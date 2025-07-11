package com.pro.salehero.admins.article.controller.dto

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
)
