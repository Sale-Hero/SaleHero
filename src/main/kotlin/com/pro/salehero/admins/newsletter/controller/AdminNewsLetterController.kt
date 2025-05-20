package com.pro.salehero.newsletter.controller

import com.pro.salehero.admins.newsletter.controller.dto.SendTestMailDTO
import com.pro.salehero.newsletter.service.AdminNewsLetterService
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterDeleteDTO
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPostDTO
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPutDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/news")
class AdminNewsLetterController(
    private val adminNewsLetterService: AdminNewsLetterService
) {
    @PostMapping
    fun createNewsLetter(
        @RequestBody dto: NewsLetterPostDTO
    ) = adminNewsLetterService.createNewsLetter(dto)

    @GetMapping
    fun getNewsLetters(
        @RequestParam(required = false) query: String?,
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 7,
            page = 1
        ) pageable: Pageable,
    ) = adminNewsLetterService.getNewsLetters(pageable, query)

//    @GetMapping("/{id}")
//    fun getNewsLetter(
//        @PathVariable("id") id: Long
//    ) = adminNewsLetterService.getNewsLetter(id)

    @PutMapping("/{idx}")
    fun updateNewsLetter(
        @RequestBody dto: NewsLetterPutDTO,
        @PathVariable("idx") idx: Long
    ) = adminNewsLetterService.updateNewsLetter(
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
    ) = adminNewsLetterService.removeNewsLetters(dto)

    @PostMapping("/mock")
    fun sendToMeTestMail(
        @RequestBody dto: SendTestMailDTO,
    ) = adminNewsLetterService.sendMockMail(dto)
}