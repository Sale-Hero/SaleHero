package com.pro.salehero.domain.verification

import org.springframework.data.jpa.repository.JpaRepository

interface EmailVerificationTokenRepository : JpaRepository<EmailVerificationToken, Long> {
    fun findByTokenHash(tokenHash: String): EmailVerificationToken?
}
