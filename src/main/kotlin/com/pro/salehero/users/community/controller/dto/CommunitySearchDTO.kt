package com.pro.salehero.users.community.controller.dto

import com.pro.salehero.domain.community.enums.CommunityCategory


data class CommunitySearchDTO(
    val category: CommunityCategory?,
)
