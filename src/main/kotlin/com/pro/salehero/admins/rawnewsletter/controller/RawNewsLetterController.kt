package com.pro.salehero.admins.rawnewsletter.controller

import com.pro.salehero.admins.newsletter.controller.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.controller.dto.RawNewsLetterDTO
import com.pro.salehero.admins.rawnewsletter.service.RawNewsLetterService
import com.pro.salehero.common.dto.PageResponseDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/raw")
class RawNewsLetterController(
    private val rawNewsLetterService: RawNewsLetterService
) {

    @PostMapping("/generate")
    fun generate(
        @RequestBody rawNewsLetterPostDTO: RawNewsLetterPostDTO
    ) = rawNewsLetterService.generateRawNewsLetter(rawNewsLetterPostDTO)

    @GetMapping
    fun getRawNewsLetters(
        @PageableDefault(
            sort = ["createdAt"],
            direction = Sort.Direction.DESC,
            size = 15,
            page = 1
        ) pageable: Pageable,
    ): PageResponseDTO<RawNewsLetterDTO> = rawNewsLetterService.getRawNewsLetters(pageable)
}