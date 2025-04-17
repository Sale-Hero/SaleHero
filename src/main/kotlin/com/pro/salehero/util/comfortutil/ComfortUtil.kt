package com.pro.salehero.util.comfortutil

import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class ComfortUtil {

    // 이메일 타입 검증
    fun validateEmail(email: String) {
        val emailRegex = Regex("""^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""")

        if (email.isBlank()) {
            throw CustomException(ErrorCode.CODE_404)
        }

        if (!emailRegex.matches(email)) {
            throw CustomException(ErrorCode.CODE_4000)
        }
    }
}