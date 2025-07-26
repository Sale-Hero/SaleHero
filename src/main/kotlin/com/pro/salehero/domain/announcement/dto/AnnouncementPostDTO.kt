package com.pro.salehero.domain.announcement.dto

import com.pro.salehero.common.enums.AnnouncementCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AnnouncementPostDTO(
    @field:NotBlank
    @field:NotNull
    val title: String,
    @field:NotBlank
    @field:NotNull
    val content: String,
    @field:NotNull
    val category: AnnouncementCategory,

    @field:NotNull
    val isVisible: String
)
