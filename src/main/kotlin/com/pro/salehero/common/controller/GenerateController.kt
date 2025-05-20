package com.pro.salehero.common.controller

import com.pro.salehero.admins.admin.service.AdminService
import com.pro.salehero.admins.rawnewsletter.controller.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.service.RawNewsLetterService
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPostDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/automate")
class GenerateController (
    private val rawNewsLetterService: RawNewsLetterService,
    private val adminService: AdminService,
){
    @PostMapping("/raw")
    fun generate(
        @RequestBody rawNewsLetterPostDTO: RawNewsLetterPostDTO
    ) = rawNewsLetterService.generateRawNewsLetter(rawNewsLetterPostDTO)

    @PostMapping("/news-letter")
    fun createNewsLetter(
        @RequestBody dto: NewsLetterPostDTO
    ) = adminService.createNewsLetter(dto)
}