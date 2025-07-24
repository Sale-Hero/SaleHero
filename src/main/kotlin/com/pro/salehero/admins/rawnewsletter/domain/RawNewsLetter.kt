package com.pro.salehero.admins.rawnewsletter.domain

import com.pro.salehero.users.community.domain.enums.ContentsCategory
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
