package com.pro.salehero.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityDebugFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(SecurityDebugFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        log.error("요청 시작: ${request.method} ${request.requestURI}")

        try {
            filterChain.doFilter(request, response)
            log.error("요청 완료: ${response.status}")
        } catch (e: Exception) {
            log.error("필터 체인에서 예외 발생: ${e.message}", e)
            throw e
        }
    }
}