package com.pro.salehero.domain.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    // 필요에 따라 추가 쿼리 메서드 정의 가능 (예: 특정 채팅방의 메시지 조회)
}