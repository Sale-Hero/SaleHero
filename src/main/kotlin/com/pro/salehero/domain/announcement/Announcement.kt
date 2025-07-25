package com.pro.salehero.domain.announcement

import com.pro.salehero.common.enums.AnnouncementCategory
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class Announcement (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,
    @Lob // text
    var content: String,

    @Enumerated(EnumType.STRING)
    var category: AnnouncementCategory,

    @Column(nullable = true, length = 1)
    var isVisible: String? = "N",

    @Column(nullable = true, length = 1)
    var isDeleted: String? = "N",
    val viewCount: Long = 0,
): CreateAndUpdateAudit()
