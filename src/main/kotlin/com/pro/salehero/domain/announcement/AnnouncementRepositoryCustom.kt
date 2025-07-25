package com.pro.salehero.domain.announcement

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.domain.announcement.dto.AdminAnnouncementDTO
import com.pro.salehero.domain.announcement.dto.UserAnnouncementDTO
import org.springframework.data.domain.Pageable

interface AnnouncementRepositoryCustom {
    fun getAdminAnnouncements(
        pageable: Pageable
    ): PageResponseDTO<AdminAnnouncementDTO>

    fun getUserAnnouncements(
        pageable: Pageable
    ): PageResponseDTO<UserAnnouncementDTO>
}
