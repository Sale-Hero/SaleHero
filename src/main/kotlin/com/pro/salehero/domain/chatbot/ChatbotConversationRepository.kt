package com.pro.salehero.domain.chatbot

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatbotConversationRepository: JpaRepository<ChatbotConversation, Long>
