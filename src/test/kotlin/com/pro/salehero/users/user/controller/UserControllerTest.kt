package com.pro.salehero.users.user.controller


import com.pro.salehero.users.user.dto.UserResponseDTO
import com.pro.salehero.domain.user.enums.UserRole
import com.pro.salehero.users.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.core.user.OAuth2User

@ExtendWith(MockitoExtension::class)
class UserControllerTest {

    @InjectMocks
    private lateinit var userController: UserController

    @Mock
    private lateinit var userService: UserService

    @Test
    fun `getCurrentUser - 현재 사용자 조회 성공`() {
        // given
        val mockPrincipal = mock<OAuth2User>()
        val response = UserResponseDTO(
            1L,
            "테스터",
            "test@gmail.com",
            "테스터 히히",
            UserRole.USER
        )
        given(userService.getUserInfo(mockPrincipal))
            .willReturn(ResponseEntity.ok(response))

        // when
        val result = userController.getCurrentUser(mockPrincipal)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}
