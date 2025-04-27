package com.pro.salehero.auth.service

import com.pro.salehero.auth.controller.dto.MailRequestDTO
import com.pro.salehero.auth.controller.dto.MailVerifyDTO
import com.pro.salehero.auth.domain.UserAuthentication
import com.pro.salehero.auth.domain.UserAuthenticationRepository
import com.pro.salehero.auth.service.dto.OauthUserInfo
import com.pro.salehero.auth.service.dto.TokenResponseDTO
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.config.JwtTokenProvider
import com.pro.salehero.subscribe.domain.subscriber.SubscribeRepository
import com.pro.salehero.subscribe.domain.subscriber.Subscriber
import com.pro.salehero.user.domain.User
import com.pro.salehero.user.domain.UserRepository
import com.pro.salehero.user.domain.enums.UserRole
import com.pro.salehero.util.comfortutil.ComfortUtil
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URLEncoder
import java.time.Duration
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val comfortUtil: ComfortUtil,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userAuthenticationRepository: UserAuthenticationRepository,
    private val subscribeRepository: SubscribeRepository,
    @Value("\${target.origins}") private val origin: String,
) {

    @Transactional
    fun findOrCreateUser(userInfo: OauthUserInfo): User {
        val existingUser = userRepository.findByUserEmail(userInfo.email)

        // 기존 사용자가 있으면 반환
        if (existingUser != null) {
            return existingUser
        }

        // 새 사용자 생성
        val userRole = if (userInfo.email.equals("pnci1029@gmail.com")) UserRole.ADMIN else UserRole.USER
        val newUser = User(
            userEmail = userInfo.email,
            userName = userInfo.name,
            isActive = "Y",
            nickName = comfortUtil.nickNameCreator(),
            role = userRole
        )

        // 저장 후 반환
        return userRepository.save(newUser)
    }

    @Transactional
    fun generateToken(user: User): TokenResponseDTO {
        return jwtTokenProvider.createTokens(user)
    }

    fun loginSuccess(jwtToken: TokenResponseDTO, response: HttpServletResponse) {
        val params = mapOf(
            "accessToken" to jwtToken.accessToken,
            "refreshToken" to jwtToken.refreshToken
        )

        val queryString = params.entries.joinToString("&") { (key, value) ->
            "$key=${URLEncoder.encode(value, "UTF-8")}"
        }
        val authUrl = "$origin/success?$queryString"
        response.sendRedirect(authUrl)
    }

    fun createNewTokenByRefreshToken(
        refreshToken: String
    ) = jwtTokenProvider.createNewAccessToken(refreshToken)

    @Transactional
    fun createCodeByEmail(dto: MailRequestDTO): ResponseDTO<Boolean> {
        // 이미 구독중인지
        subscribeRepository.findByUserEmail(dto.userEmail)
            .takeIf { it.isNotEmpty() }
            ?.let { return ResponseDTO(false, "이미 구독중인 회원입니다.", false) }

        // 이미 코드를 받았는지
        val auth = userAuthenticationRepository.findByEmail(dto.userEmail)
            ?: UserAuthentication(email = dto.userEmail, code = createAuthCode())
                .also { userAuthenticationRepository.save(it) }

        auth.code = createAuthCode()
//        auth.updatedAt = LocalDateTime.now()

        return ResponseDTO(
            success = true,
            message = "인증 메일이 발송되었습니다.",
            data = null
        )
    }

    @Transactional
    fun verifyAuthCode(dto: MailVerifyDTO): ResponseDTO<Boolean> {
        isAlreadySubscribed(dto.userEmail)
        val auth = isAuthCodePresent(dto.userEmail)

        // 인증시간 검증
        if (isAuthTimeExceeded(authTime = auth.updatedAt)) {
            return ResponseDTO(
                success = false,
                message = "인증 시간이 만료되었습니다. 다시 시도해주세요.",
                data = false
            )
        }

        if (auth.code != dto.code) {
            throw CustomException(ErrorCode.CODE_4002)
        }

        subscribeRepository.save(
            Subscriber(
                userEmail = dto.userEmail,
                isSubscribed = "Y"
            )
        )
        return ResponseDTO(true, "구독 완료했습니다.", true)
    }


    private fun createAuthCode(): String {
        val charPool = ('A'..'Z') + ('0'..'9')
        return (1..8)
            .map { charPool.random() }
            .joinToString("")
    }

    private fun isAuthCodePresent(
        userEmail: String
    ): UserAuthentication {
        val auth = userAuthenticationRepository.findByEmail(userEmail)

        if (auth == null) {
            throw CustomException(ErrorCode.CODE_4041)
        }

        return auth
    }

    private fun isAlreadySubscribed(
        userEmail: String
    ) = subscribeRepository.findByUserEmail(userEmail).takeIf { it.isNotEmpty() }
        ?.let { throw CustomException(ErrorCode.CODE_4001) }

    private fun isAuthTimeExceeded(authTime: LocalDateTime)
            : Boolean {
        val currentTime = LocalDateTime.now()
        val minutesDifference = Duration.between(authTime, currentTime).toMinutes()

        println("minutesDifference = ${minutesDifference}")
        return minutesDifference > 2
    }
}