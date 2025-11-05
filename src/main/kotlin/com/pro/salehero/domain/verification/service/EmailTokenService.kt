package com.pro.salehero.domain.verification.service

import com.pro.salehero.domain.verification.EmailVerificationToken
import com.pro.salehero.domain.verification.EmailVerificationTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

@Service
class EmailTokenService(
    private val emailVerificationTokenRepository: EmailVerificationTokenRepository,
) {

    private val tokenExpiryHours = 24L

    @Transactional
    fun generateAndSaveToken(rawNewsLetterId: Long): String {
        val token = UUID.randomUUID().toString()
        val tokenHash = hashToken(token)

        val expiryDate = LocalDateTime.now().plusHours(tokenExpiryHours)

        val verificationToken = EmailVerificationToken(
            tokenHash = tokenHash,
            rawNewsLetterId = rawNewsLetterId,
            expiryDate = expiryDate
        )

        emailVerificationTokenRepository.save(verificationToken)

        return token // Return the raw token to be used in the email link
    }

    @Transactional
    fun verifyTokenAndGetNewsLetterId(token: String): Long {
        val tokenHash = hashToken(token)
        val verificationToken = emailVerificationTokenRepository.findByTokenHash(tokenHash)
            ?: throw RuntimeException("유효하지 않은 토큰입니다.") // Or a more specific exception

        if (verificationToken.expiryDate.isBefore(LocalDateTime.now())) {
            emailVerificationTokenRepository.delete(verificationToken)
            throw RuntimeException("만료된 토큰입니다.")
        }

        val rawNewsLetterId = verificationToken.rawNewsLetterId
        
        // Invalidate the token after use
        emailVerificationTokenRepository.delete(verificationToken)

        return rawNewsLetterId
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray(Charsets.UTF_8))
        return hashBytes.fold("") { str, it -> str + "%02x".format(it) }
    }
}
