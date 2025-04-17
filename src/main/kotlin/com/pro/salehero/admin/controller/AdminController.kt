package com.pro.salehero.admin.controller

import com.pro.salehero.admin.service.AdminService
import com.pro.salehero.newsletter.controller.dto.NewsLetterPostDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController @RequestMapping("/api/admin")
class AdminController (
    val adminService: AdminService
){
    @PostMapping("/news-letter")
    fun createNewsLetter(
        @RequestBody dto: NewsLetterPostDTO
    ) = adminService.createNewsLetter(dto)
}