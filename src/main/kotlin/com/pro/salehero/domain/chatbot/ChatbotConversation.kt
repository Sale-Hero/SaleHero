package com.pro.salehero.domain.chatbot

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ChatbotConversation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val question: String,
    val answer: String,
    val chatHistoryIdx: Long,
)
