package com.pro.salehero.config

import com.pro.salehero.common.PageResponseDTO
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

abstract class QueryDslSupport(
    protected val queryFactory: JPAQueryFactory
) {
    protected fun <T> fetchPageResponse( // 페이징 공통함수
        contentQuery: JPAQuery<T>,
        countQuery: JPAQuery<Long>,
        pageable: Pageable
    ): PageResponseDTO<T> {
        val page = clientPageConverter(pageable)

        val paginatedQuery = contentQuery
            .offset(page.offset)
            .limit(page.pageSize.toLong())

        // 결과 조회
        val content = paginatedQuery.fetch()
        val total = countQuery.fetchOne() ?: 0L
        val totalPages = if (total == 0L) 0 else
            (total / page.pageSize + if (total % page.pageSize > 0) 1 else 0).toInt()

        return PageResponseDTO(
            totalPages = totalPages,
            totalElement = total,
            content = content
        )
    }

    private fun clientPageConverter(
        pageable: Pageable
    ): Pageable {
        return PageRequest.of(
            pageable.pageNumber - 1,
            pageable.pageSize,
            pageable.sort
        )
    }
}