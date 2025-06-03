package com.pro.salehero.users.community.controller.dto

import com.pro.salehero.users.community.domain.enums.CommunityCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CommunityPostDTO(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String,

    @field:NotNull(message = "카테고리는 필수입니다.")
    val category: CommunityCategory
)
