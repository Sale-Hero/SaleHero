package com.pro.salehero.domain.announcement

import org.springframework.data.jpa.repository.JpaRepository

interface AnnouncementRepository : JpaRepository<Announcement, Long>, AnnouncementRepositoryCustom
