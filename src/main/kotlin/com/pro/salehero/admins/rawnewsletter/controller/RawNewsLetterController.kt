package com.pro.salehero.admins.rawnewsletter.controller

import com.pro.salehero.admins.newsletter.controller.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.service.RawNewsLetterService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController @RequestMapping("/api/admin/raw")
class RawNewsLetterController (
    private val rawNewsLetterService: RawNewsLetterService
){

    @PostMapping
    fun generate(
        @RequestBody rawNewsLetterPostDTO: RawNewsLetterPostDTO
    ) = rawNewsLetterService.generateRawNewsLetter(rawNewsLetterPostDTO)
}