package com.pro.salehero.users.chatbot.doomain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class ChatbotConversation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val question: String,
    val answer: String,
    val chatHistoryIdx: Long,
)