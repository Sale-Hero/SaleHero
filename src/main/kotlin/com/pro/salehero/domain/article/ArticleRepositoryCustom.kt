package com.pro.salehero.domain.article

import com.pro.salehero.domain.article.dto.AdminArticleDTO
import com.pro.salehero.domain.article.dto.UserArticleDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.dto.ViewCount
import org.springframework.data.domain.Pageable

interface ArticleRepositoryCustom {
    fun getAdminArticles(pageable: Pageable): PageResponseDTO<AdminArticleDTO>

    fun getUserArticles(pageable: Pageable): PageResponseDTO<UserArticleDTO>

    fun updateViewCount(viewCount: ViewCount)
}
