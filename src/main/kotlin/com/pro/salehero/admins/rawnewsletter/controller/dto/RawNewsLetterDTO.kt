package com.pro.salehero.admins.rawnewsletter.controller.dto

import com.pro.salehero.admins.rawnewsletter.domain.RawNewsLetter

data class RawNewsLetterDTO(
    val idx: Long,
    val title: String,
    val content: String,
    val createdAt: String,
){
    companion object {
        fun of(rawNewsLetter: RawNewsLetter): RawNewsLetterDTO {
            return RawNewsLetterDTO(
                idx = rawNewsLetter.id!!,
                title = rawNewsLetter.title,
                content = rawNewsLetter.content,
                createdAt = rawNewsLetter.createdAt.toString()
            )
        }
    }
}
