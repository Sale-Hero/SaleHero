package com.pro.salehero.users.article.service

import com.pro.salehero.users.article.domain.ArticleRepository
import org.springframework.stereotype.Service

@Service
class UserArticleService (
    private val userArticleRepository: ArticleRepository
){
}
