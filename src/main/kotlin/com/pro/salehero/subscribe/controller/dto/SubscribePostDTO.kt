package com.pro.salehero.subscribe.controller.dto

import com.pro.salehero.subscribe.domain.NewsLetterFrequency

data class SubscribePostDTO(
    val userEmail: String,
    val frequency: NewsLetterFrequency,
    val isMarketingAgreed: Boolean
)