package com.pro.salehero.community.controller.dto

import com.pro.salehero.community.domain.enums.CommunityCategory

data class CommunityPostDTO(
    val title: String,
    val content: String,
    val category: CommunityCategory
)
