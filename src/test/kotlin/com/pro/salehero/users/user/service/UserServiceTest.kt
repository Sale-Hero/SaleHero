package com.pro.salehero.users.user.service

import com.pro.salehero.config.IntegrationTestSupport
import com.pro.salehero.users.user.controller.dto.UserResponseDTO
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.UserRepository
import com.pro.salehero.users.user.domain.enums.UserRole
import org.assertj.core.api.Assertions.assertThat
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.OAuth2User
import kotlin.test.Test

class UserServiceTest: IntegrationTestSupport() {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `getUserInfo - 정상적인 사용자 정보 조회`() {
        // given
        val user = createAndSaveUser()
        val mockPrincipal = createMockOAuth2User(user.id!!)

        // when
        val response = userService.getUserInfo(mockPrincipal)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body as UserResponseDTO
        assertThat(body.id).isEqualTo(user.id)
        assertThat(body.userEmail).isEqualTo(user.userEmail)
        assertThat(body.nickName).isEqualTo(user.nickName)
    }

    @Test
    fun `getUserInfo - principal이 null인 경우 401 에러 발생`() {
        // given
        val nullPrincipal: OAuth2User ?= null

        // when
        val result = userService.getUserInfo(nullPrincipal)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        val body = result.body as Map<String, String>
        assertThat(body["error"]).isEqualTo("Unauthorized")
        assertThat(body["message"]).isEqualTo("로그인이 필요합니다.")
    }

    @Test
    fun `getUserInfo -  principal에 id가 없는 경우 400 에러 발생`() {
        // given
        val mockPrincipal = mock<OAuth2User>()
        given(mockPrincipal.getAttribute<Int>("id")).willReturn(null)

        // when
        val result = userService.getUserInfo(mockPrincipal)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val body = result.body as Map<String, String>
        assertThat(body["error"]).isEqualTo("Bad Request")
        assertThat(body["message"]).isEqualTo("사용자 ID가 유효하지 않습니다.")
    }

    private fun createAndSaveUser(): User {
        val user = User(
            userEmail = "test@example.com",
            userName = "testUser",
            nickName = "테스트유저",
            isActive = "Y",
            role = UserRole.USER
        )
        return userRepository.save(user)
    }

    private fun createMockOAuth2User(userId: Long?): OAuth2User {
        val mockPrincipal = mock<OAuth2User>()
        whenever(mockPrincipal.getAttribute<Int>("id")).thenReturn(userId?.toInt())
        return mockPrincipal
    }
}
