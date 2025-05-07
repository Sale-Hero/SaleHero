package com.pro.salehero.newsletter.controller

import com.pro.salehero.newsletter.service.UserNewsLetterService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/news")
class UserNewsLetterController(
    val userNewsLetterService: UserNewsLetterService
) {
    @GetMapping
    fun getNewsLetters(
        @RequestParam(required = false) query: String?,
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 7,
            page = 1
        ) pageable: Pageable,
    ) = userNewsLetterService.getNewsLetters(pageable, query)

    @GetMapping("/{id}")
    fun getNewsLetter(
        @PathVariable("id") id: Long
    ) = userNewsLetterService.getNewsLetter(id)

}