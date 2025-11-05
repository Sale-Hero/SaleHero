package com.pro.salehero.common.dto

import com.pro.salehero.common.enums.RedisContentType

data class ViewCount(
    val type: RedisContentType,
    val id: Long,
    val viewCount: Long
)
