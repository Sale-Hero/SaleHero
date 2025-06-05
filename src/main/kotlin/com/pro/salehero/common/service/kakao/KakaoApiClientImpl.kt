package com.pro.salehero.common.service.kakao

import org.springframework.beans.factory.annotation.Value

class KakaoApiClientImpl(
    @Value("\${kakao.clientId}") private val clientId: String,
    @Value("\${kakao.clientSecret}") private val clientSecret: String,
): KakaoApiClient {
    override fun getToken(): String {
        TODO("Not yet implemented")
    }
}