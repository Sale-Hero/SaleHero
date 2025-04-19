package com.pro.salehero.common.scheduling

import com.pro.salehero.newsletter.domain.NewsLetterRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.time.LocalDate

@Component
class NewsLetterScheduler(
    val newsLetterRepository: NewsLetterRepository,

    @Value("\${smtp.email}") private val sender: String,

    private var javaMailSender: JavaMailSender
) {

    @Transactional
//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0 11 * * *", zone = "Asia/Seoul")  // at 12:00
    fun sendSaleInformationMail() {
        val todayNewLetters = newsLetterRepository.findTodayNewsLetter(LocalDate.now().atStartOfDay())

        // Thymeleaf 템플릿 엔진 설정
        val templateEngine = setupThymeleafTemplate()

        todayNewLetters?.forEach { newsLetter ->
            val title = newsLetter.title
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

            // 템플릿에 바인딩할 컨텍스트 생성
            val context = Context()

            // 뉴스레터 데이터를 템플릿 변수에 설정
            context.setVariable("dealTitle", newsLetter.title)
            context.setVariable("dealId", newsLetter.id)
            context.setVariable("email", "pnci1029@gmail.com")
//            context.setVariable("token", generateToken("pnci1029@gmail.com"))  // 토큰 생성 함수 필요

            // HTML 템플릿 처리
            val emailContent = templateEngine.process("saleInformation", context)

            helper.setFrom(sender)
            helper.setTo("pnci1029@gmail.com")
            helper.setSubject("세일히어로 - 오늘의 할인정보: $title")
            helper.setText(emailContent, true)  // true는 HTML 형식 의미

            javaMailSender.send(mimeMessage)
                .also { newsLetterRepository.updateToSent(newsLetter) }
                .also { println("Sent email for ${newsLetter.title}") }
        }
    }

    private fun setupThymeleafTemplate():SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/"  // 템플릿 파일 경로
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = "UTF-8"
        templateEngine.setTemplateResolver(templateResolver)

        return templateEngine
    }

    // 사용자 인증을 위한 간단한 토큰 생성 함수
//    private fun generateToken(email: String): String {
//        // 실제 구현에서는 보안을 위해 더 강력한 방법 사용 필요
//        return Base64.getEncoder().encodeToString("$email:${System.currentTimeMillis()}".toByteArray())
//    }
}