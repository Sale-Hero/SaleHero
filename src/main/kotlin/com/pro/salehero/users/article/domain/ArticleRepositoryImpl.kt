package com.pro.salehero.users.article.domain

import com.pro.salehero.admins.article.controller.dto.AdminArticleDTO
import com.pro.salehero.admins.article.controller.dto.UserArticleDTO
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

    override fun getAdminArticles(
        pageable: Pageable
    ): PageResponseDTO<AdminArticleDTO> =
        fetchPageResponse(
            pageable,
            queryFactory
                .select(
                    Projections.constructor(
                        AdminArticleDTO::class.java,
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
        )

    override fun getUserArticles(
        pageable: Pageable
    ): PageResponseDTO<UserArticleDTO> =
        fetchPageResponse(
            pageable,
            queryFactory
                .select(
                    Projections.constructor(
                        UserArticleDTO::class.java,
                        article.id,
                        article.title,
                        article.content,
                        article.summary,
                        article.category,
                        article.viewCount,
                        article.createdAt,
                    )
                )
                .from(article)
                .where(
                    article.isVisible.eq("Y"),
                    article.isDeleted.eq("N")
                )
                .orderBy(article.createdAt.desc())
        )

    override fun updateViewCount(viewCount: ViewCount) {
        queryFactory
            .update(article)
            .set(article.viewCount, viewCount.viewCount)
            .where(article.id.eq(viewCount.id))
            .execute()
    }
}
