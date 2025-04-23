package com.pro.salehero.auth.controller

import com.pro.salehero.auth.controller.dto.TokenChangeDTO
import com.pro.salehero.auth.service.GoogleAuthService
import com.pro.salehero.auth.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService,
    val googleAuthService: GoogleAuthService // 구글 인증 처리용 서비스
) {

    @GetMapping("/google")
    fun googleAuthUrl(
        response: HttpServletResponse,
    ) = googleAuthService.generateAuthorizationUrl(response)

    @GetMapping("/google/callback")
    fun googleCallback(
        @RequestParam code: String,
        response: HttpServletResponse,
    ) {
        val tokenInfo = googleAuthService.getAccessToken(code)

        // 토큰을 이용해 사용자 정보 가져오기
        val userInfo = googleAuthService.getUserInfo(tokenInfo.accessToken)

        // 해당 이메일로 가입된 사용자가 있는지 확인 후 로그인 또는 회원가입 처리
        val user = authService.findOrCreateUser(userInfo)

        // JWT 토큰 생성 및 반환
        val jwtToken = authService.generateToken(user)

        authService.loginSuccess(jwtToken, response)
    }

    @PostMapping("/token")
    fun createNewToken(
        @RequestBody dto: TokenChangeDTO
    ) = authService.createNewTokenByRefreshToken(dto.refreshToken)
}