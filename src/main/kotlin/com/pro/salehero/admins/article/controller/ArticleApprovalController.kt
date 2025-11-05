package com.pro.salehero.admins.article.controller

import com.pro.salehero.admins.article.service.AdminArticleService
import com.pro.salehero.domain.verification.service.EmailTokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/article")
class ArticleApprovalController(
    private val emailTokenService: EmailTokenService,
    private val adminArticleService: AdminArticleService
) {

    @GetMapping("/approve")
    fun approveArticleFromEmail(@RequestParam token: String): ResponseEntity<String> {
        return try {
            val rawNewsLetterId = emailTokenService.verifyTokenAndGetNewsLetterId(token)
            adminArticleService.approveArticleFromRawNewsLetter(rawNewsLetterId)
            ResponseEntity.ok("성공적으로 승인되었습니다.") // Ideally, redirect to a nice HTML page
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
