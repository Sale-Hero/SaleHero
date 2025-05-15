package com.pro.salehero.users.user.controller

import com.pro.salehero.users.user.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    val userService: UserService,
) {
    @GetMapping("/current-user")
    fun getCurrentUser(
        @AuthenticationPrincipal principal: OAuth2User?
    ) = userService.getUserInfo(principal)
}