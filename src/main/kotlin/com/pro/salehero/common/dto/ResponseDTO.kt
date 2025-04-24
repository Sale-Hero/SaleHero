package com.pro.salehero.common.dto

data class ResponseDTO<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
) {
    companion object {
        // 성공 응답 생성 헬퍼 메소드
        fun <T> success(message: String, data: T? = null): ResponseDTO<T> {
            return ResponseDTO(true, message, data)
        }

        // 실패 응답 생성 헬퍼 메소드
        fun <T> error(message: String, data: T? = null): ResponseDTO<T> {
            return ResponseDTO(false, message, data)
        }
    }
}