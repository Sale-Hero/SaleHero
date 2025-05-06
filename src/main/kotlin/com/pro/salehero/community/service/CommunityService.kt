package com.pro.salehero.community.service

import com.pro.salehero.community.controller.dto.CommunityPostDTO
import com.pro.salehero.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.community.domain.Community
import com.pro.salehero.community.domain.CommunityRepository
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val communityRepository: CommunityRepository,
) {
    fun createArticle(
        communityPostDTO: CommunityPostDTO
    ): CommunityResponseDTO {
        val user = getCurrentUser()

        val community = Community(
            title = communityPostDTO.title,
            content = communityPostDTO.content,
            category = communityPostDTO.category,
            writerId = user.id!!,
            viewCount = 0,
            isDeleted = "N",
        )

        val result = communityRepository.save(community)

        return CommunityResponseDTO(
            result.id!!,
            result.title,
            result.content,
            result.createdAt,
            result.viewCount,
            user.nickName,
        )
    }

    fun getArticles(
        dto: CommunitySearchDTO,
        pageable: Pageable
    ) = communityRepository.getArticles(dto, pageable)


}