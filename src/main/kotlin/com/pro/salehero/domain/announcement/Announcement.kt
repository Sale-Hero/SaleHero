package com.pro.salehero.domain.announcement

import com.pro.salehero.common.enums.AnnouncementCategory
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class Announcement () : CreateAndUpdateAudit() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    lateinit var title: String

    @Lob // text
    lateinit var content: String

    @Enumerated(EnumType.STRING)
    lateinit var category: AnnouncementCategory

    @Column(nullable = false, length = 1)
    var isVisible: String = "N"

    @Column(nullable = false, length = 1)
    var isDeleted: String = "N"
    val viewCount: Long = 0
}
