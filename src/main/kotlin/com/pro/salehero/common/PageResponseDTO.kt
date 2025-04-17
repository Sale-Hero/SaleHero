package com.pro.salehero.common

data class PageResponseDTO<T>(
    val totalPages: Int,
    val totalElement: Long,
    val content: List<T>,
    )
