package com.pro.salehero.subscribe.controller.dto

import com.pro.salehero.subscribe.domain.Subscriber
import java.time.LocalDateTime

data class SubscriberResponseDTO(
    val idx: Long,
    val userEmail: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(subscriber: Subscriber): SubscriberResponseDTO {
            return SubscriberResponseDTO(
                idx = subscriber.id!!,
                userEmail = subscriber.userEmail,
                createdAt = subscriber.createdAt,
            )
        }
    }
}