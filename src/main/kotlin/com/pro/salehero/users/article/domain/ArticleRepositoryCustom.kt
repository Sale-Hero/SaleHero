package com.pro.salehero.users.article.domain

import com.pro.salehero.common.dto.PageResponseDTO

interface ArticleRepositoryCustom {
    fun getArticles(): PageResponseDTO<Article>
}
