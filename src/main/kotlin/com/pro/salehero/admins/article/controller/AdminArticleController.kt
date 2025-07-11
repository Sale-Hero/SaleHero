package com.pro.salehero.admins.article.controller

import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.admins.article.service.AdminArticleService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/article")
class AdminArticleController (
    private val adminArticleService: AdminArticleService,
){
    @PostMapping
    fun createAdminArticle(
        @Valid @RequestBody dto: AdminArticlePostDTO,
    ) = ResponseEntity.ok(adminArticleService.createAdminArticle(dto))

    @GetMapping
    fun getAdminArticles(
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 7,
            page = 1
        ) pageable: Pageable,
    ) = adminArticleService.getAdminArticles(pageable)
}
