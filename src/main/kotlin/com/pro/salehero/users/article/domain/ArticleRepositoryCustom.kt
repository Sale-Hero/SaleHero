package com.pro.salehero.users.article.domain

import com.pro.salehero.admins.article.controller.dto.AdminArticleDTO
import com.pro.salehero.common.dto.PageResponseDTO
import org.springframework.data.domain.Pageable

interface ArticleRepositoryCustom {
    fun getArticles(pageable: Pageable): PageResponseDTO<AdminArticleDTO>
}
