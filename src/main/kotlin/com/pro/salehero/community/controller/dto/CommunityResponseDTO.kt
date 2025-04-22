package com.pro.salehero.community.controller.dto

import java.time.LocalDateTime

data class CommunityResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val viewCount: Long,

    val writerName: String,
    )
