package com.pro.salehero.users.chatbot.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ChatHistoryRepository: JpaRepository<ChatHistory, Long>{
    fun findBySessionIdAndCreatedAtAfter(sessionId: String, createdAt: LocalDateTime): ChatHistory?
    fun findByIpAndCreatedAtAfter(ip: String, createdAt: LocalDateTime): ChatHistory?

}
