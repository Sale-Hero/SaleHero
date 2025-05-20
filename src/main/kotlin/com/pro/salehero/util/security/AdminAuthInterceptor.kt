package com.pro.salehero.util.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AdminAuthInterceptor(
    @Value("\${jwt.secret}") private val secretKey: String
) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(AdminAuthInterceptor::class.java)
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.debug("Admin Auth 인터셉터 실행: ${request.requestURI}")

        // JWT 토큰 확인 (헤더에서 가져옴)
        val authToken = getTokenFromRequest(request)

        // 토큰이 없는 경우 권한 없음
        if (authToken.isNullOrEmpty()) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("관리자 권한이 필요합니다.")
            return false
        }

        // 토큰 검증 및 관리자 권한 확인
        try {
            val claims = extractClaims(authToken)
            logger.debug("토큰 검증 성공: ${claims.subject}")

            // 역할 확인
            val role = claims["role"]?.toString()
            logger.debug("사용자 역할: $role")

            // 관리자 권한이 아닌 경우 접근 거부
            if (role != "ADMIN") {
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.writer.write("관리자 권한이 없습니다.")
                return false
            }

            return true
        } catch (e: ExpiredJwtException) {
            logger.error("만료된 JWT 토큰: ${e.message}")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("만료된 인증 토큰입니다.")
            return false
        } catch (e: MalformedJwtException) {
            logger.error("잘못된 형식의 JWT 토큰: ${e.message}")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("유효하지 않은 인증 토큰입니다.")
            return false
        } catch (e: SignatureException) {
            logger.error("JWT 서명 검증 실패: ${e.message}")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("유효하지 않은 인증 토큰입니다.")
            return false
        } catch (e: Exception) {
            logger.error("JWT 처리 중 오류 발생: ${e.message}", e)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("인증 처리 중 오류가 발생했습니다.")
            return false
        }
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }

        // 쿼리 파라미터에서도 토큰 확인 (테스트용)
        val tokenParam = request.getParameter("token")
        if (tokenParam != null) {
            return tokenParam
        }

        return null
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}