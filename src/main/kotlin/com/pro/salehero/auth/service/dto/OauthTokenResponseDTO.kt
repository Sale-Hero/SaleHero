package com.pro.salehero.auth.service.dto

data class OauthTokenResponseDTO(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String? = null,
    val token_type: String
)
