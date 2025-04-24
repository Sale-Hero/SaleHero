package com.pro.salehero.newsletter.controller

import com.pro.salehero.newsletter.controller.dto.NewsLetterDeleteDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPostDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.newsletter.service.NewsLetterService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/news")
class NewsLetterController(
    val newsLetterService: NewsLetterService
) {
    @PostMapping
    fun createNewsLetter(
        @RequestBody dto: NewsLetterPostDTO
    ) = newsLetterService.createNewsLetter(dto)

    @GetMapping
    fun getNewsLetters(
        @RequestParam(required = false) query: String?,
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 7,
            page = 1
        ) pageable: Pageable,
    ) = newsLetterService.getNewsLetters(pageable, query)

    @PutMapping("/{idx}")
    fun updateNewsLetter(
        @RequestBody dto: NewsLetterPutDTO,
        @PathVariable("idx") idx: Long
    ) = newsLetterService.updateNewsLetter(
        NewsLetterPutDTO(
            idx = idx,
            title = dto.title,
            content = dto.content,
            sentAt = dto.sentAt,
        )
    )

    @DeleteMapping
    fun removeNewsLetter(
        @RequestBody dto: NewsLetterDeleteDTO,
    ) = newsLetterService.removeNewsLetters(dto)
}