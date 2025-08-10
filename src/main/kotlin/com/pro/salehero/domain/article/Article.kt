package com.pro.salehero.domain.article

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
        title: String,
        content: String,
        summary: String,
        category: ContentsCategory,
        isVisible: String?,
    ) {
        this.title = title
        this.content = content
        this.summary = summary
        this.category =category
        this.isVisible = isVisible
    }
}
