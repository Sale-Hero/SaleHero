package com.pro.salehero.domain.newsletter

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.config.QueryDslSupport
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterResponseDTO
import com.pro.salehero.domain.newsletter.QNewsLetter.newsLetter
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class NewsLetterRepositoryImpl(
    queryFactory: JPAQueryFactory
) : QueryDslSupport(queryFactory), NewsLetterRepositoryCustom {

    override fun searchUserNewsLetter(
        pageable: Pageable,
        query: String?
    ): PageResponseDTO<NewsLetterResponseDTO> {
        // isPublic이 'Y'인 뉴스레터만 검색하도록 조건 추가
        return searchNewsLetter(pageable, query, true)
    }

    override fun searchAdminNewsLetter(
        pageable: Pageable,
        query: String?
    ): PageResponseDTO<NewsLetterResponseDTO> {
        // 관리자는 모든 뉴스레터를 볼 수 있도록 isPublic 조건 없이 검색
        return searchNewsLetter(pageable, query, false)
    }

    private fun searchNewsLetter(
        pageable: Pageable,
        query: String?,
        onlyPublic: Boolean
    ): PageResponseDTO<NewsLetterResponseDTO> {
        // 공통 프로젝션 설정
        return fetchPageResponse(
            pageable,
            queryFactory
                .select(
                    Projections.constructor(
                        NewsLetterResponseDTO::class.java,
                        newsLetter.id,
                        newsLetter.title,
                        newsLetter.content,
                        newsLetter.isSent,
                        newsLetter.isPublic,
                        newsLetter.sentAt,
                        newsLetter.createdAt
                    )
                )
                .from(newsLetter)
                .where(
                    searchKeywordContains(query),
                    if (onlyPublic) newsLetter.isPublic.eq("Y") else null
                )
                .orderBy(newsLetter.createdAt.desc())
        )
    }

    override fun updateNewsLetter(
        dto: NewsLetterPutDTO
    ) = queryFactory
        .update(newsLetter)
        .set(newsLetter.title, dto.title)
        .set(newsLetter.content, dto.content)
        .set(newsLetter.sentAt, dto.sentAt)
        .where(newsLetter.id.eq(dto.idx))
        .execute()

    override fun remove(
        idx: Long
    ) = queryFactory
        .delete(newsLetter)
        .where(newsLetter.id.eq(idx))
        .execute()


    override fun findTodayNewsLetter(
        today: LocalDateTime
    ): MutableList<NewsLetterResponseDTO> = queryFactory
        .select(
            Projections.constructor(
                NewsLetterResponseDTO::class.java,
                newsLetter.id,
                newsLetter.title,
                newsLetter.content,
                newsLetter.isSent,
                newsLetter.sentAt,
                newsLetter.createdAt
            )
        )
        .from(newsLetter)
        .where(
            newsLetter.sentAt.goe(today),
            newsLetter.sentAt.loe(today.plusDays(1)),
            newsLetter.isSent.eq("N")
        )
        .fetch()

    override fun updateToSent(
        dto: NewsLetterResponseDTO
    ) {
        queryFactory
            .update(newsLetter)
            .set(newsLetter.isSent, "Y")
            .set(newsLetter.isPublic, "Y")
            .where(newsLetter.id.eq(dto.id))
            .execute()
    }

    // 검색어를 타이틀이나 컨텐츠에서 찾는 조건 함수
    private fun searchKeywordContains(query: String?): BooleanExpression? {
        return if (!query.isNullOrBlank()) {
            newsLetter.title.containsIgnoreCase(query)
                .or(newsLetter.content.containsIgnoreCase(query))
        } else {
            null
        }
    }

    private fun getTotalPages(total: Long, pageSize: Int): Int {
        return if (total == 0L) 0 else
            (total / pageSize + if (total % pageSize > 0) 1 else 0).toInt()
    }
}
