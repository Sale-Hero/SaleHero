package com.pro.salehero.newsletter.service

import com.pro.salehero.admins.newsletter.controller.dto.SendTestMailDTO
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.common.service.MailSenderService
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterDeleteDTO
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPostDTO
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterPutDTO
import com.pro.salehero.users.newsletter.domain.NewsLetter
import com.pro.salehero.users.newsletter.domain.NewsLetterRepository
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.naming.Context

@Service
class AdminNewsLetterService(
    private val newsLetterRepository: NewsLetterRepository,
    private val mailSenderService: MailSenderService,
    @Value("\${smtp.email}") private val sender: String,
) {
    @Transactional
    fun createNewsLetter(
        dto: NewsLetterPostDTO
    ) = getCurrentUser()
        .let {
            NewsLetter(
                title = dto.title,
                content = dto.content,
                isSent = "N",
                isPublic = "N",
                sentAt = null
            )
        }
        .let { newsLetterRepository.save(it) }
        .also { ResponseEntity.ok() }

    @Transactional(readOnly = true)
    fun getNewsLetters(
        pageable: Pageable,
        query: String?,
    ) = newsLetterRepository.searchAdminNewsLetter(pageable, query)

    @Transactional
    fun updateNewsLetter(
        dto: NewsLetterPutDTO
    ) = getCurrentUser()
        .let { newsLetterRepository.updateNewsLetter(dto) }
        .let {
            if (it >= 1) {
                ResponseEntity.ok().body(it)
            } else {
                throw CustomException(ErrorCode.CODE_404)
            }
        }

    @Transactional
    fun removeNewsLetters(dto: NewsLetterDeleteDTO): ResponseDTO<Long> {
        var deletedCount = 0L

        dto.idxList.forEach { idx ->
            val result = newsLetterRepository.remove(idx)
            deletedCount += result
        }

        return ResponseDTO(
            success = true,
            message = "뉴스레터 삭제가 완료되었습니다.",
            data = deletedCount
        )
    }

    @Transactional
    fun sendMockMail(
        dto: SendTestMailDTO
    ) {
        val newsLetter = newsLetterRepository.findById(dto.id)
            .orElseThrow { CustomException(ErrorCode.CODE_404) }

        val context = org.thymeleaf.context.Context()
        context.setVariable("dealTitle", newsLetter.title)
        context.setVariable("dealId", newsLetter.id)
        context.setVariable("email", dto.target)

        mailSenderService.sendEmail(
            to = dto.target ?:"pnci1029@gmail.com",
            subject = "테스트용 메일입니다.",
            templateName = "saleInformationV2",
            context = context,
            mailSender = sender
        )
        println("끝")
    }
}