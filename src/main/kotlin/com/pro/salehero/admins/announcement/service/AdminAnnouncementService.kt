package com.pro.salehero.admins.announcement.service

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.domain.announcement.AnnouncementRepository
import com.pro.salehero.domain.announcement.dto.AdminAnnouncementDTO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminAnnouncementService (
    private val announcementRepository: AnnouncementRepository,
){

    @Transactional(readOnly = true)
    fun getAdminAnnouncements(
        pageable: Pageable
    ): PageResponseDTO<AdminAnnouncementDTO> = announcementRepository.getAdminAnnouncements(pageable)
}
