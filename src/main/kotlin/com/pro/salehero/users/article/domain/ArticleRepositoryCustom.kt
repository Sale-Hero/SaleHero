package com.pro.salehero.users.article.domain

import com.pro.salehero.admins.article.controller.dto.AdminArticleDTO
import com.pro.salehero.admins.article.controller.dto.UserArticleDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.service.dto.ViewCount
import org.springframework.data.domain.Pageable

interface ArticleRepositoryCustom {
    fun getAdminArticles(pageable: Pageable): PageResponseDTO<AdminArticleDTO>

    fun getUserArticles(pageable: Pageable): PageResponseDTO<UserArticleDTO>

    fun updateViewCount(viewCount: ViewCount)
}
