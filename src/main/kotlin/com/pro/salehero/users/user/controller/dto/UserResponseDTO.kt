package com.pro.salehero.users.user.controller.dto

import com.pro.salehero.users.user.domain.enums.UserRole

data class UserResponseDTO(
    val id: Long,
    val userName: String,
    val userEmail: String,
    val nickName: String,
    val role: UserRole
)
