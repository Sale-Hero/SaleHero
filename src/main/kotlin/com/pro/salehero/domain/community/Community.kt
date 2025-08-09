package com.pro.salehero.domain.community

import com.pro.salehero.domain.community.enums.CommunityCategory
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class Community (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    val content: String,
    val category: CommunityCategory,

    val writerId: Long,
    val viewCount: Long,

    @Column(nullable = true, length = 1)
    val isDeleted: String = "Y",
): CreateAndUpdateAudit()

