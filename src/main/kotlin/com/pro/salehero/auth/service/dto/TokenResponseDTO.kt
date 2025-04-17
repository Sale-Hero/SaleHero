package com.pro.salehero.auth.service.dto

data class TokenResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long
)
