package com.pro.salehero.util.security

import com.pro.salehero.user.domain.User
import com.pro.salehero.user.domain.enums.UserRole
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class SecurityUtil {

    companion object {
        fun getCurrentUser(): User {
            val authentication = SecurityContextHolder.getContext().authentication
                ?: throw CustomException(ErrorCode.CODE_403)

            val principal = authentication.principal
            val oAuth2User = when (principal) {
                is OAuth2User -> principal
                else -> throw CustomException(ErrorCode.CODE_403)
            }

            return userConverter(oAuth2User)
        }

        private fun userConverter(oAuth2User: OAuth2User):User {
            val id = oAuth2User.getAttribute<Int>("id")?.toLong() ?: 0L
            val userEmail = oAuth2User.getAttribute<String>("userEmail") ?: ""
            val userName = oAuth2User.getAttribute<String>("userName") ?: ""
            val nickName = oAuth2User.getAttribute<String>("nickName") ?: ""
            val role = oAuth2User.getAttribute<String>("role") ?: "USER"

            return User(id, userEmail, userName, nickName,"Y", UserRole.valueOf(role))
        }
    }

}