package com.pro.salehero.users.community.domain

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.users.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import org.springframework.data.domain.Pageable

interface CommunityRepositoryCustom {
    fun getArticles(
        dto: CommunitySearchDTO,
        pageable: Pageable
    ): PageResponseDTO<CommunityResponseDTO>
}