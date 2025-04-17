package com.pro.salehero.newsletter.domain

import com.pro.salehero.common.PageResponseDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.newsletter.controller.dto.NewsLetterResponseDTO
import com.pro.salehero.newsletter.domain.QNewsLetter.newsLetter
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class NewsLetterRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : NewsLetterRepositoryCustom {

    override fun searchNewsLetter(
        pageable: Pageable,
        query: String?
    ): PageResponseDTO<NewsLetterResponseDTO> {
        val result = queryFactory
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
                searchKeywordContains(query)
            )
            .orderBy(newsLetter.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val content = result.fetch()

        val countQuery = queryFactory
            .select(newsLetter.count())
            .from(newsLetter)
            .where(
                searchKeywordContains(query)
            )

        val total = countQuery.fetchOne() ?: 0L
        val totalPages = getTotalPages(total, pageable.pageSize)

        return PageResponseDTO(
            totalPages = totalPages,
            totalElement = total,
            content = content
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
        )
        .fetch()

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