package com.pro.salehero.common.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.util.concurrent.CompletableFuture

@Service
class MailSenderService(
    @Value("\${smtp.email}") private val sender: String,
    @Value("\${smtp.noreply-email}") private val noReplySender: String,
    private val javaMailSender: JavaMailSender
) {
    // 이메일 템플릿 엔진 초기화 (싱글톤으로 한 번만 생성)
    private val templateEngine: SpringTemplateEngine by lazy {
        setupThymeleafTemplate()
    }

    /**
     * 이메일 발송 기본 메서드
     * @param to 수신자 이메일
     * @param subject 이메일 제목
     * @param templateName 사용할 템플릿 이름
     * @param context Thymeleaf 컨텍스트 (템플릿에 전달할 변수들)
     * @return 이메일 발송 성공 여부
     */
    fun sendEmail(
        to: String,
        subject: String,
        templateName: String,
        context: Context,
        mailSender: String
    ): Boolean {
        return try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

            // HTML 템플릿 처리
            val emailContent = templateEngine.process(templateName, context)

            helper.setFrom(mailSender)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(emailContent, true) // true는 HTML 형식 의미

            // 실제 이메일 발송
            javaMailSender.send(mimeMessage)
            true
        } catch (e: Exception) {
            // 로깅 추가 필요
            println("Failed to send email: ${e.message}")
            false
        }
    }

    /**
     * 이메일 발송을 비동기로 처리하는 메서드
     */
    fun sendEmailAsync(
        to: String,
        subject: String,
        templateName: String,
        context: Context
    ): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            sendEmail(to, subject, templateName, context, sender)
        }
    }

    /**
     * 인증 코드 이메일 발송 편의 메서드
     */
    fun sendVerificationEmail(
        to: String,
        verificationCode: String
    ): Boolean {
        val context = Context()
        context.setVariable("verificationCode", verificationCode)

        return sendEmail(
            to = to,
            subject = "세일히어로 - 이메일 인증",
            templateName = "mailAuthentication",
            context = context,
            mailSender = noReplySender
        )
    }

    /**
     * 할인 정보 이메일 발송 편의 메서드
     */
    fun sendSaleInfoEmail(
        to: String,
        dealTitle: String,
        dealId: Long,
        email: String,
        token: String? = null
    ): Boolean {
        val context = Context()
        context.setVariable("dealTitle", dealTitle)
        context.setVariable("dealId", dealId)
        context.setVariable("email", email)
        token?.let { context.setVariable("token", it) }

        return sendEmail(
            to = to,
            subject = "세일히어로 - 오늘의 할인정보: $dealTitle",
            templateName = "saleInformation",
            context = context,
            mailSender = sender
        )
    }

    private fun setupThymeleafTemplate(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/"  // 템플릿 파일 경로
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = "UTF-8"
        templateEngine.setTemplateResolver(templateResolver)

        return templateEngine
    }
}