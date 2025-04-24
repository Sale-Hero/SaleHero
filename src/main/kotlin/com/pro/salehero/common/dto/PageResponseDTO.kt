package com.pro.salehero.common.dto

data class PageResponseDTO<T>(
    val totalPages: Int,
    val totalElement: Long,
    val content: List<T>,
    )
