package com.pro.salehero.admins.article.controller

import com.pro.salehero.admins.article.controller.dto.AdminArticleDeleteDTO
import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.admins.article.service.AdminArticleService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/{articleId}")
    fun modifyArticle(
        @PathVariable articleId: Long,
        @Valid @RequestBody dto: AdminArticlePostDTO,
    ) = ResponseEntity.ok(adminArticleService.modifyAdminArticle(articleId, dto))

    @DeleteMapping
    fun deleteArticle(
        @RequestBody dto: AdminArticleDeleteDTO,
    ) = ResponseEntity.ok(adminArticleService.deleteArticle(dto))
}
