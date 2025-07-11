package com.pro.salehero.config

import com.pro.salehero.common.dto.PageResponseDTO
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
abstract class QueryDslSupport(
    protected val queryFactory: JPAQueryFactory
) {
    protected fun <T> fetchPageResponse(
        pageable: Pageable,
        baseQuery: JPAQuery<T>
    ): PageResponseDTO<T> {
        val adjustedPageable = PageRequest.of(
            maxOf(0, pageable.pageNumber - 1),
            pageable.pageSize,
            pageable.sort
        )

        // 컨텐츠 쿼리
        val content = baseQuery.clone()
            .offset(adjustedPageable.offset)
            .limit(adjustedPageable.pageSize.toLong())
            .fetch()

        // 카운트 쿼리
        val total = baseQuery.clone()
            .select(Expressions.ONE.count())
            .fetchOne() ?: 0L

        val totalPages = if (total == 0L) 0 else (total + adjustedPageable.pageSize - 1) / adjustedPageable.pageSize

        return PageResponseDTO(
            totalPages = totalPages.toInt(),
            totalElement = total,
            content = content
        )
    }
}
