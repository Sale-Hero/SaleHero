package com.pro.salehero.community.domain

import com.pro.salehero.community.domain.enums.CommunityCategory
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*


@Entity
data class Community (
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