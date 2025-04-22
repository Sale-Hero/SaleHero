package com.pro.salehero.community.service

import com.pro.salehero.community.controller.dto.CommunityPostDTO
import com.pro.salehero.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.community.domain.CommunityRepository
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val communityRepository: CommunityRepository,
) {
    fun createArticle(communityPostDTO: CommunityPostDTO): Any {
        val user = getCurrentUser()
        println(user.userName)

        return ""
    }


    fun getArticles(
        dto: CommunitySearchDTO,
        pageable: Pageable
    ) =  communityRepository.getArticles(dto, pageable)

}