package com.pro.salehero.users.article.domain

import com.pro.salehero.users.community.domain.enums.ArticleCategory
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class Article (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    @Lob // text
    val content: String,
    val summary: String,

    @Enumerated(EnumType.STRING)
    val category: ArticleCategory,

    @Column(nullable = true, length = 1)
    val isVisible: String? = "N",

    @Column(nullable = true, length = 1)
    val isDeleted: String? = "N",
    val viewCount: Long = 0,
) : CreateAndUpdateAudit()
