package com.pro.salehero.users.article.controller

import com.pro.salehero.users.article.service.UserArticleService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/articles")
class UserArticleController (
    private val userArticleService: UserArticleService,
){
    @GetMapping
    fun getUserArticles(
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 7,
            page = 0
        ) pageable: Pageable,
    ) = userArticleService.getUserArticles(pageable)
}
