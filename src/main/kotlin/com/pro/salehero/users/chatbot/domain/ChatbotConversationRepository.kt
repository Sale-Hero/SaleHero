package com.pro.salehero.users.chatbot.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatbotConversationRepository: JpaRepository<ChatbotConversation, Long>
