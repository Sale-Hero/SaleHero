package com.pro.salehero.users.article.controller

import com.pro.salehero.users.article.service.UserArticleService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/articles")
class UserArticleController (
    private val userArticleService: UserArticleService,
){
}
