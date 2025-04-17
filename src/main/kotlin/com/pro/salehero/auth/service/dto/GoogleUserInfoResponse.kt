package com.pro.salehero.auth.service.dto

data class GoogleUserInfoResponse(
    val id: String,
    val email: String,
    val verified_email: Boolean,
    val name: String,
    val given_name: String?,
    val family_name: String?,
    val picture: String?
)
