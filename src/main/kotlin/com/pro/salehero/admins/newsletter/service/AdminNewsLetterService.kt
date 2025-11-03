package com.pro.salehero.newsletter.service

import com.pro.salehero.admins.newsletter.dto.SendTestMailDTO
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.common.service.MailSenderService
import com.pro.salehero.users.newsletter.dto.NewsLetterDeleteDTO
import com.pro.salehero.users.newsletter.dto.NewsLetterPostDTO
import com.pro.salehero.users.newsletter.dto.NewsLetterPutDTO
import com.pro.salehero.domain.newsletter.NewsLetter
import com.pro.salehero.domain.newsletter.NewsLetterRepository
import com.pro.salehero.users.subscribe.service.UnsubscribeTokenService
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import com.pro.salehero.auth.security.SecurityUtil.Companion.getCurrentUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminNewsLetterService(
    private val newsLetterRepository: NewsLetterRepository,
    private val mailSenderService: MailSenderService,
    private val unsubscribeTokenService: UnsubscribeTokenService,
    @Value("\${smtp.email}") private val sender: String,
    @Value("\${smtp.noreply-email}") private val noReplySender: String,
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
                sentAt = null,
                viewCount = 0,
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

        val token = unsubscribeTokenService.generateUnsubscribeToken(dto.target)

        val context = org.thymeleaf.context.Context()
        context.setVariable("dealTitle", newsLetter.title)
        context.setVariable("dealId", newsLetter.id)
        context.setVariable("email", dto.target)
        context.setVariable("token", token)


        mailSenderService.sendEmail(
            to = dto.target ?:"pnci1029@gmail.com",
            subject = "테스트용 메일입니다.",
            templateName = "saleInformationV2",
            context = context,
            mailSender = noReplySender
        )
    }
}
