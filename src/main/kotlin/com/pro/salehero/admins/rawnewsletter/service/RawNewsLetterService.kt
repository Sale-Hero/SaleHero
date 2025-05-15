package com.pro.salehero.admins.rawnewsletter.service

import com.pro.salehero.admins.newsletter.controller.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.domain.RawNewsLetter
import com.pro.salehero.admins.rawnewsletter.domain.RawNewsLetterRepository
import com.pro.salehero.common.dto.ResponseDTO
import org.springframework.stereotype.Service

@Service
class RawNewsLetterService(
    private val rawNewsLetterRepository: RawNewsLetterRepository
) {
    fun generateRawNewsLetter(
        rawNewsLetterPostDTO: RawNewsLetterPostDTO
    ): ResponseDTO<Boolean> = RawNewsLetter(
        title = rawNewsLetterPostDTO.title,
        content = rawNewsLetterPostDTO.content,
    )
        .also { rawNewsLetterRepository.save(it) }
        .let { ResponseDTO(false, "데이터 추가 완료", false) }

}