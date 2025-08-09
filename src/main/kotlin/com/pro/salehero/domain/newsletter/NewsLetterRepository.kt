package com.pro.salehero.domain.newsletter

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsLetterRepository: JpaRepository<NewsLetter, Long>, NewsLetterRepositoryCustom {
}