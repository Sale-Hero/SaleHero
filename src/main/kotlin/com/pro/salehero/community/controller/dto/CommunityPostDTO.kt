package com.pro.salehero.community.controller.dto

import com.pro.salehero.community.domain.enums.CommunityCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CommunityPostDTO(
    @field:NotBlank
    @field:NotNull
    val title: String,

    @field:NotBlank
    @field:NotNull
    val content: String,

    @field:NotBlank
    @field:NotNull
    val category: CommunityCategory
)
