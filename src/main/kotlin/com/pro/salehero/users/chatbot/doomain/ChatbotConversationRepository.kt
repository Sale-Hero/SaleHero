package com.pro.salehero.users.chatbot.doomain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatbotConversationRepository: JpaRepository<ChatbotConversation, Long>