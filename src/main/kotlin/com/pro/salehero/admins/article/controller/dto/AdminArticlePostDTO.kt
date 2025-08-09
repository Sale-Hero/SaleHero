package com.pro.salehero.admins.article.controller.dto

import com.pro.salehero.domain.community.enums.ContentsCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AdminArticlePostDTO (
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,
    val summary: String,

    @field:NotNull
    val category: ContentsCategory,

    val isVisible: String,
)
