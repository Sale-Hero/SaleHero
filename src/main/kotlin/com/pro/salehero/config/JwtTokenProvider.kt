package com.pro.salehero.config

import com.pro.salehero.user.domain.User
import com.pro.salehero.auth.service.dto.TokenResponseDTO
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.ACCESS_TOKEN_EXPIRED}") private val accessTokenValidTime: Int,
    @Value("\${jwt.REFRESH_TOKEN_EXPIRED}") private val refreshTokenValidTime: Int
) {
    // 시크릿 키 생성
    private val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createTokens(user: User): TokenResponseDTO {
        val now = Date()
        val accessTokenExpiration = Date(now.time + accessTokenValidTime.toLong() * 1000 * 60)
        val refreshTokenExpiration = Date(now.time + refreshTokenValidTime.toLong() * 1000 * 60)

        // 사용자 추가 정보 맵
        val additionalClaims =
            mapOf(
                "id" to user.id,
                "userEmail" to user.userEmail,
                "userName" to user.userName,
                "role" to user.role.name
            )

        // Access Token 생성
        val accessToken = Jwts.builder()
            .setSubject(user.userEmail)
            .addClaims(additionalClaims)
            .setIssuedAt(now)
            .setExpiration(accessTokenExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        // Refresh Token 생성
        val refreshToken = Jwts.builder()
            .setSubject(user.userEmail)
            .setIssuedAt(now)
            .setExpiration(refreshTokenExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiration.time
        )
    }
}