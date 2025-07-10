package com.pro.salehero.admins.article.controller.dto

import com.pro.salehero.users.community.domain.enums.ArticleCategory

data class AdminArticlePostDTO (
    val title: String,
    val content: String,
    val summary: String,
    val category: ArticleCategory,
)
