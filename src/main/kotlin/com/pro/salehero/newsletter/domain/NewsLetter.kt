package com.pro.salehero.newsletter.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class NewsLetter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    @Lob // text
    val content: String,

    @Column(nullable = true, length = 1)
    val isSent: String = "N",

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val sentAt: LocalDateTime,

) : CreateAndUpdateAudit()