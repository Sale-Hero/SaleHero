package com.pro.salehero.users.user.controller

import com.pro.salehero.common.service.kakao.KakaoApiClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController @RequestMapping("/test")
class TestController(
    private val kakaoApiClient: KakaoApiClient
) {
    @GetMapping
    fun test(): String = "Hello World1"

    @GetMapping("/kakao-test")
    fun kakaoTest(): String {
        val result =kakaoApiClient.getToken()
        return result
    }

    @GetMapping("/mail-test")
    fun mailTest() {

    }
}
