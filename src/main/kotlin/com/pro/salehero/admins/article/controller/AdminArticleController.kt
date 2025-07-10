package com.pro.salehero.admins.article.controller

import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.admins.article.service.AdminArticleService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/articles")
class AdminArticleController (
    private val adminArticleService: AdminArticleService,
){
    @PostMapping
    fun createArticle(
        @RequestBody dto: AdminArticlePostDTO,
    ) = adminArticleService.createArticle(dto)
}
