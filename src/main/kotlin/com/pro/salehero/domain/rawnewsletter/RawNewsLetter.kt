package com.pro.salehero.domain.rawnewsletter

import com.pro.salehero.domain.community.enums.ContentsCategory
import com.pro.salehero.util.CreateAudit
import jakarta.persistence.*

@Entity
class RawNewsLetter (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    @Lob // text
    val content: String,

    @Enumerated(EnumType.STRING)
    val category: ContentsCategory,
    val articleUrl: String,
    val keyword: String

    ): CreateAudit()
