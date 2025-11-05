package com.pro.salehero.common.support.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SupportPostDTO(
    @field:NotBlank
    @field:NotNull
    val title: String,

    @field:NotBlank
    @field:NotNull
    val content: String,

    @field:NotBlank
    @field:NotNull
    val name: String,

    @field:NotBlank
    @field:NotNull
    val email: String,
    val cellPhone: String?,
)
