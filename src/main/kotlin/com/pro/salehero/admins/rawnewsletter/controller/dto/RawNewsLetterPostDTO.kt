package com.pro.salehero.admins.rawnewsletter.controller.dto

import com.pro.salehero.admins.rawnewsletter.domain.enums.NewsLetterCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RawNewsLetterPostDTO(
    val id: Long?,
    @field:NotBlank
    @field:NotNull
    val title: String,

    @field:NotBlank
    @field:NotNull
    val content: String,

    @field:NotBlank
    @field:NotNull
    val category: NewsLetterCategory,

    val articleUrl: String,
    val keyword: String
)