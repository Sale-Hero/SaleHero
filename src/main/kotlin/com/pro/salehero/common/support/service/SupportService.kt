package com.pro.salehero.common.support.service

import com.pro.salehero.common.support.controller.dto.SupportPostDTO
import com.pro.salehero.common.support.domain.Support
import com.pro.salehero.common.support.domain.SupportRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class SupportService (
    private val supportRepository: SupportRepository
){
    fun createSupport(
        dto: SupportPostDTO
    ): ResponseEntity<Void> {
        supportRepository.save(
            Support(
                title = dto.title,
                content = dto.content,
                name = dto.name,
                email = dto.email,
                cellPhone = dto.cellPhone?:"",
            )
        )

        return ResponseEntity(HttpStatus.CREATED)
    }
}