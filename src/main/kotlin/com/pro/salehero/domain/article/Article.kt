package com.pro.salehero.domain.article

import com.pro.salehero.admins.article.controller.dto.AdminArticlePostDTO
import com.pro.salehero.domain.community.enums.ContentsCategory
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,
    @Lob // text
    var content: String,
    var summary: String,

    @Enumerated(EnumType.STRING)
    var category: ContentsCategory,

    @Column(nullable = true, length = 1)
    var isVisible: String? = "N",

    @Column(nullable = true, length = 1)
    var isDeleted: String? = "N",
    val viewCount: Long = 0,
) : CreateAndUpdateAudit() {
    fun update(
        articlePostDTO: AdminArticlePostDTO
    ) {
        this.title = articlePostDTO.title
        this.content = articlePostDTO.content
        this.summary = articlePostDTO.summary
        this.category = articlePostDTO.category
        this.isVisible = articlePostDTO.isVisible
    }
}
