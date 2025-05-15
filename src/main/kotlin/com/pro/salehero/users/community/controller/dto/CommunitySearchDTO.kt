package com.pro.salehero.users.community.controller.dto

import com.pro.salehero.community.domain.enums.CommunityCategory

data class CommunitySearchDTO(
    val category: CommunityCategory?,
)
