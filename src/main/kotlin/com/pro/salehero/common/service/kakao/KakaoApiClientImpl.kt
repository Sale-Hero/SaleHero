package com.pro.salehero.common.service.kakao

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


@Component
class KakaoApiClientImpl(
    @Value("\${kakao.clientId}") private val clientId: String,
    @Value("\${kakao.clientSecret}") private val clientSecret: String,
): KakaoApiClient {

    companion object {
        private const val TOKEN_BASE_URL = "https://bizmsg-web.kakaoenterprise.com/v2/oauth/token"


    }

    private val restTemplate = RestTemplate()

    override fun getToken(): String {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.parseMediaType("*/*"))
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            set("Authorization", "Basic $clientId $clientSecret")
        }

        val body: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "client_credentials")
        }

        val request = HttpEntity(body, headers)
        val response = restTemplate.postForEntity(TOKEN_BASE_URL, request, String::class.java)

        return response.body ?: throw RuntimeException("토큰 요청 실패")
    }
}