package com.pro.salehero.domain.chat

import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
@Table(name = "chat_message")
class ChatMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING) // Add annotation
    val type: MessageType, // Type is now implicitly from the same package
    val sender: String,
    val content: String?
) : CreateAndUpdateAudit()