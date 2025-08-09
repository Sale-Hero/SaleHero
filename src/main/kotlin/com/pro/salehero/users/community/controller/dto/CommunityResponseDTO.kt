package com.pro.salehero.users.community.controller.dto

import com.pro.salehero.domain.community.Community
import java.time.LocalDateTime

data class CommunityResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val viewCount: Long,

    val writerName: String,
){
    companion object {
        fun of(community: Community): CommunityResponseDTO {
            return CommunityResponseDTO(
                id = community.id!!,
                title = community.title,
                content = community.content,
                viewCount = community.viewCount,
                writerName = "히히",
                createdAt = community.createdAt
            )
        }
    }
}
