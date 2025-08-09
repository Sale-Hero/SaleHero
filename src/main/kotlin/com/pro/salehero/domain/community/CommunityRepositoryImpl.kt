package com.pro.salehero.users.community.domain

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.service.dto.ViewCount
import com.pro.salehero.config.QueryDslSupport
import com.pro.salehero.domain.community.CommunityRepositoryCustom
import com.pro.salehero.users.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.domain.community.QCommunity.community
import com.pro.salehero.domain.community.enums.CommunityCategory
import com.pro.salehero.domain.user.QUser.user
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CommunityRepositoryImpl(
    queryFactory: JPAQueryFactory
) : QueryDslSupport(queryFactory), CommunityRepositoryCustom {

    override fun getArticles(dto: CommunitySearchDTO, pageable: Pageable): PageResponseDTO<CommunityResponseDTO> {
        //todo 풀스캔 2번 개선하세요. 패버리기전에
        return fetchPageResponse(
            pageable,
            queryFactory
                .select(
                    Projections.constructor(
                        CommunityResponseDTO::class.java,
                        community.id,
                        community.title,
                        community.content,
                        community.createdAt,
                        community.viewCount,
                        user.nickName
                    )
                )
                .from(community)
                .join(user).on(community.writerId.eq(user.id))
                .where(searchByCategory(dto.category))
                .orderBy(community.createdAt.desc())
            )
    }

    override fun increaseViewCount(viewCount: ViewCount) {
        println(viewCount.id)
        queryFactory
            .update(community)
            .set(community.viewCount, viewCount.viewCount)
            .where(community.id.eq(viewCount.id))
            .execute()
    }

    private fun searchByCategory(query: CommunityCategory?): BooleanExpression? {
        return if (query != null) {
            community.category.eq(query)
        } else {
            null
        }
    }
}
