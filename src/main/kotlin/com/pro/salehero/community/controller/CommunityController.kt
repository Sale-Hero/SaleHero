package com.pro.salehero.community.controller

import com.pro.salehero.community.controller.dto.CommunityPostDTO
import com.pro.salehero.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.community.service.CommunityService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/community")
class CommunityController (
    private val communityService: CommunityService,
){

    @PostMapping
    fun postArticle(
        @RequestBody communityPostDTO: CommunityPostDTO
    ) = communityService.createArticle(communityPostDTO)

    @GetMapping
    fun getArticles(
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 15,
            page = 0
        ) pageable: Pageable,
        @ModelAttribute dto: CommunitySearchDTO,
    ) = communityService.getArticles(dto, pageable)
}