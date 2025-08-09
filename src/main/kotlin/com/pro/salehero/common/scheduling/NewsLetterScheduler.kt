package com.pro.salehero.common.scheduling

import com.pro.salehero.common.service.MailSenderService
import com.pro.salehero.domain.newsletter.NewsLetterRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Base64

@Component
class NewsLetterScheduler(
    private val newsLetterRepository: NewsLetterRepository,
    private val mailSenderService: MailSenderService
) {

    @Transactional
//    @Scheduled(cron = "0/10 * * * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0 11 * * *", zone = "Asia/Seoul")  // at 11:00 AM
    fun sendSaleInformationMail() {
        val todayNewLetters = newsLetterRepository.findTodayNewsLetter(LocalDate.now().atStartOfDay())

        todayNewLetters?.forEach { newsLetter ->
            val email = "pnci1029@gmail.com" // 테스트용 내 메일
            val token = generateToken(email)

            // 공통화된 MailSenderService 사용
            val success = mailSenderService.sendSaleInfoEmail(
                to = email,
                dealTitle = newsLetter.title,
                dealId = newsLetter.id,
                email = email,
                token = token
            )

            if (success) {
                newsLetterRepository.updateToSent(newsLetter)
                println("Sent email for ${newsLetter.title}")
            } else {
                println("Failed to send email for ${newsLetter.title}")
            }
        }
    }

    // 사용자 인증을 위한 간단한 토큰 생성 함수
    private fun generateToken(email: String): String {
        // 실제 구현에서는 보안을 위해 더 강력한 방법 사용 필요
        return Base64.getEncoder().encodeToString("$email:${System.currentTimeMillis()}".toByteArray())
    }
}