package com.pro.salehero.domain.announcement.dto

import com.pro.salehero.common.enums.AnnouncementCategory
import java.time.LocalDateTime

data class AdminAnnouncementDTO (
    val id: Long,
    val title: String,
    val content: String,
    val category: AnnouncementCategory,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val isVisible: String?,
    val isDeleted: String?,
)
