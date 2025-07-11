package com.pro.salehero.users.article.domain

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.config.QueryDslSupport
import com.querydsl.jpa.impl.JPAQueryFactory

class ArticleRepositoryImpl(
    queryFactory: JPAQueryFactory
) : QueryDslSupport(queryFactory), ArticleRepositoryCustom {


    override fun getArticles(): PageResponseDTO<Article> {
        queryFactory
            .select()
        TODO("Not yet implemented")
    }

}
