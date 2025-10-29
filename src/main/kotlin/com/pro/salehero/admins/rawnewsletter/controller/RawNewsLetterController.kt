package com.pro.salehero.admins.rawnewsletter.controller

import com.pro.salehero.admins.rawnewsletter.dto.RawNewsLetterDTO
import com.pro.salehero.admins.rawnewsletter.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.service.RawNewsLetterService
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.users.newsletter.dto.NewsLetterDeleteDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/raw")
class RawNewsLetterController(
    private val rawNewsLetterService: RawNewsLetterService
) {

    @GetMapping
    fun getRawNewsLetters(
        @PageableDefault(
            sort = ["createdAt"],
            direction = Sort.Direction.DESC,
            size = 15,
            page = 1
        ) pageable: Pageable,
    ): PageResponseDTO<RawNewsLetterDTO> = rawNewsLetterService.getRawNewsLetters(pageable)

    @PutMapping
    fun modifyRawNewsLetter(
        @RequestBody rawNewsLetterDTO: RawNewsLetterPostDTO
    ) = rawNewsLetterService.modifyRawNewsLetter(rawNewsLetterDTO)

    @DeleteMapping
    fun deleteRawNewsLetter(
        @RequestBody dto: NewsLetterDeleteDTO
    ) = rawNewsLetterService.deleteRawNewsLetter(dto)
}