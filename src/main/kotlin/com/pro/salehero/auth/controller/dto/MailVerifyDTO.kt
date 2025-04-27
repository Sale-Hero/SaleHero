package com.pro.salehero.auth.controller.dto

data class MailVerifyDTO(
    val userEmail: String,
    val code: String
)
