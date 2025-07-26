package com.pro.salehero.admins.announcement.service

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.domain.announcement.AnnouncementRepository
import com.pro.salehero.domain.announcement.dto.AdminAnnouncementDTO
import com.pro.salehero.domain.announcement.dto.AnnouncementPostDTO
import com.pro.salehero.domain.announcement.mapper.AnnouncementMapper
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminAnnouncementService (
    private val announcementRepository: AnnouncementRepository,
    private val announcementMapper: AnnouncementMapper
){
    fun createAnnouncement(
        announcementDTO: AnnouncementPostDTO
    ): ResponseDTO<AdminAnnouncementDTO> {
        val announcement = announcementMapper.toEntity(announcementDTO)
        val createdAnnouncement = announcementRepository.save(announcement)

        return ResponseDTO(
            success = true,
            message = "등록 완료되었습니다.",
            data = announcementMapper.toAdminDTO(createdAnnouncement)
        )
    }

    @Transactional(readOnly = true)
    fun getAdminAnnouncements(
        pageable: Pageable
    ): PageResponseDTO<AdminAnnouncementDTO> = announcementRepository.getAdminAnnouncements(pageable)

}
