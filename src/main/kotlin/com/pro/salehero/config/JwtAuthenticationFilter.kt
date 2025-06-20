package com.pro.salehero.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    @Value("\${jwt.secret}") private val secretKey: String
) : OncePerRequestFilter() {

    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // JWT 검증 제외 경로 확인
        if (isExcludedPath(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = getTokenFromRequest(request)

        logger.debug("JWT 필터 실행: ${request.requestURI}, 토큰 존재: ${token != null}")

        if (token != null) {
            try {
                val claims = extractClaims(token)
                logger.debug("토큰 검증 성공: ${claims.subject}")

                // 권한 설정
                val role = claims["role"]?.toString() ?: "USER"
                val authorities = setOf(SimpleGrantedAuthority("ROLE_$role"))

                // 사용자 정보를 DefaultOAuth2User로 표현
                val attributes = mutableMapOf<String, Any>()
                claims.forEach { key, value ->
                    if (value != null) attributes[key] = value
                }

                logger.debug("사용자 속성: $attributes")

                val principal = DefaultOAuth2User(authorities, attributes, "userEmail")

                val authentication = UsernamePasswordAuthenticationToken(
                    principal, null, authorities
                )

                SecurityContextHolder.getContext().authentication = authentication
                logger.debug("인증 정보 설정 완료")
            } catch (e: ExpiredJwtException) {
                logger.error("만료된 JWT 토큰: ${e.message}")
            } catch (e: MalformedJwtException) {
                logger.error("잘못된 형식의 JWT 토큰11: ${e.message}")
            } catch (e: SignatureException) {
                logger.error("JWT 서명 검증 실패: ${e.message}")
            } catch (e: Exception) {
                logger.error("JWT 처리 중 오류 발생: ${e.message}", e)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun isExcludedPath(request: HttpServletRequest): Boolean {
        return request.requestURI.startsWith("/api/subscribe/unsubscribe")
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        if (isExcludedPath(request)) {
            return null
        }

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
