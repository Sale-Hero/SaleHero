package com.pro.salehero.community.domain

import com.pro.salehero.common.PageResponseDTO
import com.pro.salehero.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.community.domain.QCommunity.community
import com.pro.salehero.config.QueryDslSupport
import com.pro.salehero.user.domain.QUser.user
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable

class CommunityRepositoryImpl(
    queryFactory: JPAQueryFactory
) : QueryDslSupport(queryFactory), CommunityRepositoryCustom {

    override fun getArticles(dto: CommunitySearchDTO, pageable: Pageable): PageResponseDTO<CommunityResponseDTO> {
        val contentQuery = queryFactory
            .select(
                Projections.constructor(
                    CommunityResponseDTO::class.java,
                    community.id,
                    community.title,
                    community.content,
                    community.createdAt,
                    community.viewCount,
                    user.userName
                )
            )
            .from(community)
            .join(user).on(community.writerId.eq(user.id))
            .orderBy(community.createdAt.desc())

        val countQuery = queryFactory
            .select(community.count())
            .from(community)
            .join(user).on(community.writerId.eq(user.id))

        return fetchPageResponse(contentQuery, countQuery, pageable)
    }
}