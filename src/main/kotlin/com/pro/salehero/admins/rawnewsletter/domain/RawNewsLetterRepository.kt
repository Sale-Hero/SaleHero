package com.pro.salehero.admins.rawnewsletter.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface RawNewsLetterRepository : JpaRepository<RawNewsLetter, Long> {
    override fun findAll(pageable: Pageable): Page<RawNewsLetter>

    @Modifying
    @Transactional
    @Query("UPDATE RawNewsLetter r SET r.title = :title, r.content = :content WHERE r.id = :id")
    fun updateTitleAndContent(
        @Param("id") id: Long,
        @Param("title") title: String,
        @Param("content") content: String
    ): Int

    fun existsRawNewsLetterByArticleUrl(url: String): Boolean
}
