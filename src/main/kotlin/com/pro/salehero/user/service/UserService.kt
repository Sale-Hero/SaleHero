package com.pro.salehero.user.service

import com.pro.salehero.user.controller.dto.UserResponseDTO
import com.pro.salehero.user.domain.UserRepository
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUserInfo(principal: OAuth2User?): ResponseEntity<*> {
        if (principal == null) { // 비 로그인
            return ResponseEntity.status(401).body(mapOf(
                "error" to "Unauthorized",
                "message" to "로그인이 필요합니다."
            ))
        }

        // principal에서 id 추출
        val userId = principal.getAttribute<Int>("id")?.toLong() ?: run {
            return ResponseEntity.status(400).body(mapOf(
                "error" to "Bad Request",
                "message" to "사용자 ID가 유효하지 않습니다."
            ))
        }

        return userRepository.findById(userId)
            .map { user ->
                UserResponseDTO(
                    id = user.id ?: 0L,
                    userEmail = user.userEmail,
                    userName = user.userName,
                    nickName = user.nickName,
                    role = user.role
                )
            }
            .map { dto -> ResponseEntity.ok(dto) }
            .orElseThrow { throw CustomException(ErrorCode.CODE_404)}
    }
}