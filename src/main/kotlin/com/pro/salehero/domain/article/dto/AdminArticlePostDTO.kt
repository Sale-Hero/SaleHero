package com.pro.salehero.domain.article.dto

import com.pro.salehero.common.enums.ContentsCategory
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
