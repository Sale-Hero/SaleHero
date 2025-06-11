package com.pro.salehero.users.user.controller

import com.pro.salehero.common.service.kakao.KakaoApiClient
import com.pro.salehero.config.aws.AwsProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.*

@RestController @RequestMapping("/test")
class TestController(
    private val kakaoApiClient: KakaoApiClient,
    private val sesClient: SesClient,
    private val awsProperties: AwsProperties
) {
    @GetMapping
    fun test(): String = "Hello World1"

    @GetMapping("/kakao-test")
    fun kakaoTest(): String {
        val result =kakaoApiClient.getToken()
        return result
    }

    @GetMapping("/mail-test")
    fun mailTest(
        @RequestParam(value = "email", required = false) to: String = "pnci1029@gmail.com"
    ):String {
        try {
            val destination = Destination.builder()
                .toAddresses(to)
                .build()

            val subject = Content.builder()
                .data("테스트 메일")
                .build()

            val textBody = Content.builder()
                .data("안녕하세요! 이것은 테스트 메일입니다.")
                .build()

            val body = Body.builder()
                .text(textBody)
                .build()

            val message = Message.builder()
                .subject(subject)
                .body(body)
                .build()

            val emailRequest = SendEmailRequest.builder()
                .source(awsProperties.noReplyEmail)
                .destination(destination)
                .message(message)
                .build()

            val response = sesClient.sendEmail(emailRequest)
            return "메일 전송 성공: ${response.messageId()}"
        } catch (e: Exception) {
            return "메일 전송 실패: ${e.message}"
        }
    }
}
