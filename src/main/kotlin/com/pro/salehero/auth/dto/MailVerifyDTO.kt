package com.pro.salehero.auth.dto

data class MailVerifyDTO(
    val userEmail: String,
    val code: String
)
