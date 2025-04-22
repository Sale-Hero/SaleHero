package com.pro.salehero.community.domain

import com.pro.salehero.common.PageResponseDTO
import com.pro.salehero.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.community.controller.dto.CommunitySearchDTO
import org.springframework.data.domain.Pageable

interface CommunityRepositoryCustom {
    fun getArticles(
        dto: CommunitySearchDTO,
        pageable: Pageable
    ): PageResponseDTO<CommunityResponseDTO>
}