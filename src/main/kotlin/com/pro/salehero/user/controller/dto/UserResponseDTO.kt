package com.pro.salehero.user.controller.dto

import com.pro.salehero.user.domain.enums.UserRole

data class UserResponseDTO(
    val id: Long,
    val userName: String,
    val userEmail: String,
    val role: UserRole
)
