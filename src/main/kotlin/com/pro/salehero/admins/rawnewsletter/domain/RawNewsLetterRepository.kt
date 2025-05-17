package com.pro.salehero.admins.rawnewsletter.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RawNewsLetterRepository : JpaRepository<RawNewsLetter, Long> {
    override fun findAll(pageable: Pageable): Page<RawNewsLetter>
}