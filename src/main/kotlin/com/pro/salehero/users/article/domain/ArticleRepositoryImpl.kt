package com.pro.salehero.users.article.domain

import com.pro.salehero.admins.article.controller.dto.ArticleDTO
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.service.dto.ViewCount
import com.pro.salehero.config.QueryDslSupport
import com.pro.salehero.users.article.domain.QArticle.article
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable

class ArticleRepositoryImpl(
    queryFactory: JPAQueryFactory
) : QueryDslSupport(queryFactory), ArticleRepositoryCustom {

    override fun getArticles(
        pageable: Pageable,
        isAdmin: Boolean
    ): PageResponseDTO<ArticleDTO> {
        val query = queryFactory
            .select(
                Projections.constructor(
                    ArticleDTO::class.java,
                    article.id,
                    article.title,
                    article.content,
                    article.summary,
                    article.category,
                    article.viewCount,
                    article.createdAt,
                    article.isVisible,
                    article.isDeleted,
                )
            )
            .from(article)
            .orderBy(article.createdAt.desc())

        if (!isAdmin) {
            query.where(
                article.isVisible.eq("Y"),
                article.isDeleted.ne("Y")
            )
        }

        return fetchPageResponse(pageable, query)
    }

    override fun updateViewCount(viewCount: ViewCount) {
        queryFactory
            .update(article)
            .set(article.viewCount, viewCount.viewCount)
            .where(article.id.eq(viewCount.id))
            .execute()
    }
}
