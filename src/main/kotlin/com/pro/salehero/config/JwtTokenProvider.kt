package com.pro.salehero.config

import com.pro.salehero.auth.service.dto.TokenChangeResponseDTO
import com.pro.salehero.domain.user.User
import com.pro.salehero.auth.service.dto.TokenResponseDTO
import com.pro.salehero.domain.user.UserRepository
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.ACCESS_TOKEN_EXPIRED}") private val accessTokenValidTime: Int,
    @Value("\${jwt.REFRESH_TOKEN_EXPIRED}") private val refreshTokenValidTime: Int,
    private val userRepository: UserRepository,
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
                "nickName" to user.nickName,
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

    fun createNewAccessToken(refreshToken: String): TokenChangeResponseDTO {
        try {
            // 리프레시 토큰 검증
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .body

            // 토큰에서 사용자 이메일 추출
            val userEmail = claims.subject

            // 사용자 정보 조회 (사용자 리포지토리를 통해)
            val user = userRepository.findByUserEmail(userEmail)
                ?: throw CustomException(ErrorCode.CODE_4031)

            // 현재 시간과 만료 시간 설정
            val now = Date()
            val accessTokenExpiration = Date(now.time + accessTokenValidTime.toLong() * 1000 * 60)

            // 사용자 추가 정보 맵
            val additionalClaims = mapOf(
                "id" to user.id,
                "userEmail" to user.userEmail,
                "userName" to user.userName,
                "nickName" to user.nickName,
                "role" to user.role.name
            )

            // 새로운 Access Token 생성
            val newAccessToken = Jwts.builder()
                .setSubject(user.userEmail)
                .addClaims(additionalClaims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()

            return TokenChangeResponseDTO(
                accessToken = newAccessToken,
            )
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.CODE_4032)
        } catch (e: JwtException) {
            throw CustomException(ErrorCode.CODE_403)
        }
    }
}