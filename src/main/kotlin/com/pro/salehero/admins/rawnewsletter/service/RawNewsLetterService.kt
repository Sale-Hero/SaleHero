package com.pro.salehero.admins.rawnewsletter.service

import com.pro.salehero.admins.rawnewsletter.controller.dto.RawNewsLetterPostDTO
import com.pro.salehero.admins.rawnewsletter.controller.dto.RawNewsLetterDTO
import com.pro.salehero.admins.rawnewsletter.domain.RawNewsLetter
import com.pro.salehero.admins.rawnewsletter.domain.RawNewsLetterRepository
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.users.newsletter.controller.dto.NewsLetterDeleteDTO
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RawNewsLetterService(
    private val rawNewsLetterRepository: RawNewsLetterRepository
) {
    @Transactional
    fun generateRawNewsLetter(
        rawNewsLetterPostDTO: RawNewsLetterPostDTO
    ): ResponseDTO<Boolean> = RawNewsLetter(
        title = rawNewsLetterPostDTO.title,
        content = rawNewsLetterPostDTO.content,
    )
        .also { rawNewsLetterRepository.save(it) }
        .let { ResponseDTO(false, "데이터 추가 완료", false) }

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