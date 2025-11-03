package com.pro.salehero.admins.admin.controller

import com.pro.salehero.admins.admin.service.AdminService
import com.pro.salehero.users.newsletter.dto.NewsLetterPostDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
    val adminService: AdminService
) {
    @PostMapping("/news-letter")
    fun createNewsLetter(
        @RequestBody dto: NewsLetterPostDTO
    ) = adminService.createNewsLetter(dto)

    @PostMapping("/community")
    fun createArticle(

    ) {

    }
}