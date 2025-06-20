package com.pro.salehero.users.subscribe.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class UnsubscribeTokenService {

    @Value("\${jwt.unsubscribeSecret}")
    private lateinit var secretKey: String

    private val tokenValidityHours = 24L // 24시간 유효

    fun generateUnsubscribeToken(email: String): String {
        val timestamp = System.currentTimeMillis()
        val message = "$email:$timestamp"
        val signature = createSignature(message)

        return Base64.getEncoder().encodeToString("$message:$signature".toByteArray())
    }

    fun validateUnsubscribeToken(email: String, token: String): Boolean {
        return try {
            val decoded = String(Base64.getDecoder().decode(token))
            val parts = decoded.split(":")

            if (parts.size != 3) return false

            val tokenEmail = parts[0]
            val timestamp = parts[1].toLong()
            val signature = parts[2]

            // 이메일 일치 확인
            if (tokenEmail != email) return false

            // 시간 만료 확인
            val validUntil = timestamp + (tokenValidityHours * 60 * 60 * 1000)
            if (System.currentTimeMillis() > validUntil) return false

            // 서명 검증
            val expectedSignature = createSignature("$tokenEmail:$timestamp")
            signature == expectedSignature

        } catch (e: Exception) {
            false
        }
    }

    private fun createSignature(message: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secretKey.toByteArray(), "HmacSHA256"))
        return Base64.getEncoder().encodeToString(mac.doFinal(message.toByteArray()))
    }
}
