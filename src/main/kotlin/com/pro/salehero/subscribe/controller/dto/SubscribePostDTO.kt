package com.pro.salehero.subscribe.controller.dto

import com.pro.salehero.subscribe.domain.DayOfWeek

data class SubscribePostDTO(
    val userEmail: String,
    val frequency: DayOfWeek,
    val isMarketingAgreed: Boolean
)