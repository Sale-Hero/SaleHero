package com.pro.salehero.admins.rawnewsletter.service

import com.pro.salehero.admins.rawnewsletter.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.dto.RawNewsLetterDTO
import com.pro.salehero.domain.rawnewsletter.RawNewsLetter
import com.pro.salehero.domain.rawnewsletter.RawNewsLetterRepository
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.common.service.MailSenderService
import com.pro.salehero.users.newsletter.dto.NewsLetterDeleteDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RawNewsLetterService(
    private val rawNewsLetterRepository: RawNewsLetterRepository,
    private val mailSenderService: MailSenderService,

    @Value("\${admin-email}") private val adminEmail: String,
) {

    @Transactional
    fun generateRawNewsLetter(
        rawNewsLetterPostDTO: RawNewsLetterPostDTO
    ): ResponseDTO<Boolean> {
        val exists = rawNewsLetterRepository.existsRawNewsLetterByArticleUrl(rawNewsLetterPostDTO.articleUrl)

        if (exists) {
            return ResponseDTO(false, "이미 존재하는 URL입니다", false)
        }

        val rawNewsLetter = RawNewsLetter(
            title = rawNewsLetterPostDTO.title,
            content = rawNewsLetterPostDTO.content,
            category = rawNewsLetterPostDTO.category,
            articleUrl = rawNewsLetterPostDTO.articleUrl,
            keyword = rawNewsLetterPostDTO.keyword,
        )

        rawNewsLetterRepository.save(rawNewsLetter)
            // db 저장 후 메일 발송
            .also {
                mailSenderService.sendAminSelectEmail(
                    adminEmail,
                    it.id!!,
                    rawNewsLetterPostDTO.title,
                    rawNewsLetterPostDTO.content
                )
            }

        return ResponseDTO(false, "데이터 추가 완료", false)
    }

    fun getRawNewsLetters(
        pageable: Pageable
    ): PageResponseDTO<RawNewsLetterDTO> {
        val pageRequest = PageRequest.of(
            pageable.pageNumber - 1,
            pageable.pageSize,
            pageable.sort
        )
        val page = rawNewsLetterRepository.findAll(pageRequest)

        return PageResponseDTO(
            totalPages = page.totalPages,
            totalElement = page.totalElements,
            content = page.content.map { RawNewsLetterDTO.of(it) },
        )
    }

    @Transactional
    fun modifyRawNewsLetter(
        dto: RawNewsLetterPostDTO
    ) = rawNewsLetterRepository.updateTitleAndContent(dto.id!!.toLong(), dto.title, dto.content)

    @Transactional
    fun deleteRawNewsLetter(
        dto: NewsLetterDeleteDTO
    ) = dto.idxList.forEach { rawNewsLetterRepository.deleteById(it) }
}
