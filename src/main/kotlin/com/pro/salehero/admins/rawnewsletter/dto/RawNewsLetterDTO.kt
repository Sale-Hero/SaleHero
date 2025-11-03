package com.pro.salehero.admins.rawnewsletter.dto

import com.pro.salehero.domain.rawnewsletter.RawNewsLetter

data class RawNewsLetterDTO(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String,
){
    companion object {
        fun of(rawNewsLetter: RawNewsLetter): RawNewsLetterDTO {
            return RawNewsLetterDTO(
                id = rawNewsLetter.id!!,
                title = rawNewsLetter.title,
                content = rawNewsLetter.content,
                createdAt = rawNewsLetter.createdAt.toString()
            )
        }
    }
}
