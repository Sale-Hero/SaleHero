package com.pro.salehero.domain.chatbot

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
class ChatHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val ip: String? = null,
    val referer: String? = null,
    val userAgent: String? = null,
    val sessionId: String? = null,
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
