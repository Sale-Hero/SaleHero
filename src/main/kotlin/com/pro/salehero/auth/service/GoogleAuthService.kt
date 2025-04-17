package com.pro.salehero.auth.service

import com.pro.salehero.auth.service.dto.OauthUserInfo
import com.pro.salehero.auth.service.dto.GoogleUserInfoResponse
import com.pro.salehero.auth.service.dto.TokenInfo
import com.pro.salehero.auth.service.dto.OauthTokenResponseDTO
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URLEncoder

@Service
class GoogleAuthService(
    @Value("\${spring.security.oauth2.client.registration.google.client-id}") private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.google.client-secret}") private val clientSecret: String,
    @Value("\${google.auth.REDIRECT_URI}") private val redirectUri: String,
    private val restTemplate: RestTemplate
) {
    fun generateAuthorizationUrl(
        response: HttpServletResponse,
    ){
        val params = mapOf(
            "client_id" to clientId,
            "redirect_uri" to redirectUri,
            "response_type" to "code",
            "scope" to "email profile",
            "access_type" to "offline"
        )

        val queryString = params.entries.joinToString("&") { (key, value) ->
            "$key=${URLEncoder.encode(value, "UTF-8")}"
        }
        val authUrl = "https://accounts.google.com/o/oauth2/v2/auth?$queryString"

        response.sendRedirect(authUrl)
    }

    fun getAccessToken(code: String): TokenInfo {
        val url = "https://oauth2.googleapis.com/token"

        val params = mapOf(
            "code" to code,
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "redirect_uri" to redirectUri,
            "grant_type" to "authorization_code"
        )

        val response = restTemplate.postForEntity(url, params, OauthTokenResponseDTO::class.java)

        return response.body?.let {
            TokenInfo(
                accessToken = it.access_token,
                refreshToken = it.refresh_token,
                expiresIn = it.expires_in
            )
        } ?: throw RuntimeException("Failed to get access token")
    }

    fun getUserInfo(accessToken: String): OauthUserInfo {
        val url = "https://www.googleapis.com/oauth2/v2/userinfo"

        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
        }

        val entity = HttpEntity<String>(headers)
        val response = restTemplate.exchange(url, HttpMethod.GET, entity, GoogleUserInfoResponse::class.java)

        return response.body?.let {
            OauthUserInfo(
                id = it.id,
                email = it.email,
                name = it.name,
            )
        } ?: throw RuntimeException("Failed to get user info")
    }
}
