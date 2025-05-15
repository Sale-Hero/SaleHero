//package com.pro.salehero.users.community.domain
//
//import com.pro.salehero.common.dto.PageResponseDTO
//import com.pro.salehero.config.QueryDslSupport
//import com.pro.salehero.users.community.controller.dto.CommunityResponseDTO
//import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
//import com.pro.salehero.users.community.domain.enums.CommunityCategory
//import com.querydsl.core.types.Projections
//import com.querydsl.core.types.dsl.BooleanExpression
//import com.querydsl.jpa.impl.JPAQueryFactory
//import org.springframework.data.domain.Pageable
//
//class CommunityRepositoryImpl(
//    queryFactory: JPAQueryFactory
//) : QueryDslSupport(queryFactory), CommunityRepositoryCustom {
//
//    override fun getArticles(dto: CommunitySearchDTO, pageable: Pageable): PageResponseDTO<CommunityResponseDTO> {
//        //todo 풀스캔 2번 개선하세요. 패버리기전에
//        val contentQuery = queryFactory
//            .select(
//                Projections.constructor(
//                    CommunityResponseDTO::class.java,
//                    community.id,
//                    community.title,
//                    community.content,
//                    community.createdAt,
//                    community.viewCount,
//                    user.nickName
//                )
//            )
//            .from(community)
//            .join(user).on(community.writerId.eq(user.id))
//            .where(
//                searchByCategory(dto.category)
//            )
//            .orderBy(community.createdAt.desc())
//
//        val countQuery = queryFactory
//            .select(community.count())
//            .from(community)
//            .where(
//                searchByCategory(dto.category)
//            )
//            .join(user).on(community.writerId.eq(user.id))
//
//        return fetchPageResponse(contentQuery, countQuery, pageable)
//    }
//
//    private fun searchByCategory(query: CommunityCategory?): BooleanExpression? {
//        return if (query != null) {
//            community.category.eq(query)
//        } else {
//            null
//        }
//    }
//}