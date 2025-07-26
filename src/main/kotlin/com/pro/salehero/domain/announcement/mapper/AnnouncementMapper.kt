package com.pro.salehero.domain.announcement.mapper

import com.pro.salehero.domain.announcement.Announcement
import com.pro.salehero.domain.announcement.dto.AdminAnnouncementDTO
import com.pro.salehero.domain.announcement.dto.AnnouncementPostDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AnnouncementMapper {

    fun toEntity(dto: AnnouncementPostDTO): Announcement

    fun toAdminDTO(announcement: Announcement): AdminAnnouncementDTO
}
