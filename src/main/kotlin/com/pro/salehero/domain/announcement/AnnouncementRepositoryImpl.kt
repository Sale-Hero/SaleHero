package com.pro.salehero.domain.announcement

import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.config.QueryDslSupport
import com.pro.salehero.domain.announcement.QAnnouncement.announcement
import com.pro.salehero.domain.announcement.dto.AdminAnnouncementDTO
import com.pro.salehero.domain.announcement.dto.UserAnnouncementDTO
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable

class AnnouncementRepositoryImpl(
    queryFactory: JPAQueryFactory
) : QueryDslSupport(queryFactory), AnnouncementRepositoryCustom {

    override fun getAdminAnnouncements(
        pageable: Pageable
    ): PageResponseDTO<AdminAnnouncementDTO> =
        fetchPageResponse(
            pageable,
            queryFactory
                .select(
                    Projections.constructor(
                        AdminAnnouncementDTO::class.java,
                        announcement.id,
                        announcement.title,
                        announcement.content,
                        announcement.category,
                        announcement.viewCount,
                        announcement.createdAt,
                        announcement.isVisible,
                        announcement.isDeleted
                    )
                )
                .from(announcement)
                .where(
                    announcement.isDeleted.eq("N")
                )
                .orderBy(announcement.createdAt.desc())
        )

    override fun getUserAnnouncements(
        pageable: Pageable
    ): PageResponseDTO<UserAnnouncementDTO> =
        fetchPageResponse(
            pageable,
            queryFactory
                .select(
                    Projections.constructor(
                        UserAnnouncementDTO::class.java,
                        announcement.id,
                        announcement.title,
                        announcement.content,
                        announcement.category,
                        announcement.viewCount,
                        announcement.createdAt,
                    )
                )
                .from(announcement)
                .where(
                    announcement.isDeleted.eq("N"),
                    announcement.isVisible.eq("Y")
                )
                .orderBy(announcement.createdAt.desc())
        )

}
