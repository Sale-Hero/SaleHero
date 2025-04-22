package com.pro.salehero.config

import com.pro.salehero.common.PageResponseDTO
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable

abstract class QueryDslSupport(
    protected val queryFactory: JPAQueryFactory
) {
    protected fun <T> fetchPageResponse( // 페이징 공통함수
        contentQuery: JPAQuery<T>,
        countQuery: JPAQuery<Long>,
        pageable: Pageable
    ): PageResponseDTO<T> {
        val paginatedQuery = contentQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        // 결과 조회
        val content = paginatedQuery.fetch()
        val total = countQuery.fetchOne() ?: 0L
        val totalPages = if (total == 0L) 0 else
            (total / pageable.pageSize + if (total % pageable.pageSize > 0) 1 else 0).toInt()

        return PageResponseDTO(
            totalPages = totalPages,
            totalElement = total,
            content = content
        )
    }
}