package com.pro.salehero.admins.rawnewsletter.domain

import com.pro.salehero.util.CreateAudit
import jakarta.persistence.*

@Entity
data class RawNewsLetter (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    @Lob // text
    val content: String,

): CreateAudit()