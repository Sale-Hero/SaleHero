package com.pro.salehero.users.newsletter.controller

import com.pro.salehero.users.newsletter.service.UserNewsLetterService
import jakarta.servlet.http.HttpServletRequest
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
        @PathVariable("id") id: Long,
        request: HttpServletRequest
    ) = userNewsLetterService.getNewsLetter(id, request)

}