package com.pro.salehero.auth.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.HandlerInterceptor

class ApiKeyInterceptor(
    @Value("\${admin.newsletter.key}") private val apiSecret: String
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val apiKey = request.getHeader("X-API-KEY")

        if (apiKey == null || apiKey != apiSecret) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unauthorized: X-API key를 확인하세요.")
            return false
        }

        return true
    }
}