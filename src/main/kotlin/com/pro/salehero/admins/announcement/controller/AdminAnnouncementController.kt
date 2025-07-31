package com.pro.salehero.admins.announcement.controller

import com.pro.salehero.admins.announcement.service.AdminAnnouncementService
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.domain.announcement.dto.AdminAnnouncementDTO
import com.pro.salehero.domain.announcement.dto.AnnouncementPostDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/announcements")
class AdminAnnouncementController(
    private val adminAnnouncementService: AdminAnnouncementService
) {
    @PostMapping
    fun createAnnouncement(
        @RequestBody @Valid announcementDTO: AnnouncementPostDTO
    ) = adminAnnouncementService.createAnnouncement(announcementDTO)

    @GetMapping
    fun getAdminAnnouncements(
        @PageableDefault(
            sort = ["createAt"],
            direction = Sort.Direction.DESC,
            size = 7,
            page = 1
        ) pageable: Pageable,
    ): PageResponseDTO<AdminAnnouncementDTO> = adminAnnouncementService.getAdminAnnouncements(pageable)

    @PutMapping("{articleId}")
    fun updateAnnouncement(
        @RequestBody @Valid announcementDTO: AnnouncementPostDTO,
        @PathVariable("articleId") articleId: Long,
    ) = adminAnnouncementService.updateAnnouncement(announcementDTO, articleId)
}
