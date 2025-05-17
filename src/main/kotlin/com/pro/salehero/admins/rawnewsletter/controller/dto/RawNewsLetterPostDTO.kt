package com.pro.salehero.admins.rawnewsletter.controller.dto

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
)