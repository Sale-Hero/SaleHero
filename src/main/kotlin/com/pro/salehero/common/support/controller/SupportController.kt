package com.pro.salehero.common.support.controller

import com.pro.salehero.common.support.controller.dto.SupportPostDTO
import com.pro.salehero.common.support.service.SupportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/support")
class SupportController (
    private val supportService: SupportService
){
    @PostMapping
    fun createSupport(
        @RequestBody dto: SupportPostDTO
    ): ResponseEntity<Void> = supportService.createSupport(dto)
}