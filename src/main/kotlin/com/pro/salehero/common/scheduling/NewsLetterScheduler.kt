package com.pro.salehero.common.scheduling

import com.pro.salehero.newsletter.domain.NewsLetterRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class NewsLetterScheduler(
    val newsLetterRepository: NewsLetterRepository,

    @Value("\${smtp.email}") private val sender: String,

    private var javaMailSender: JavaMailSender
) {

    @Transactional
//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Seoul")  // at 12:00
    fun sendMail() {
        val todayNewLetters = newsLetterRepository.findTodayNewsLetter(LocalDate.now().atStartOfDay())

        val mimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

        todayNewLetters?.forEach{ newsLetter ->

            val title = newsLetter.title
            val subject = newsLetter.content

            helper.setFrom(sender)
            helper.setTo("pnci1029@gmail.com")
            helper.setSubject(title)
            helper.setText(subject, true)

            javaMailSender.send(mimeMessage)
                .also { newsLetterRepository.updateToSent(newsLetter) }
                .also { println("send complete") }
        }
    }
}