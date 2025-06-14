package com.pro.salehero.users.user.controller

import com.pro.salehero.users.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import kotlin.test.Test

@WebMvcTest(controllers = [UserController::class])
class UserControllerTest {

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `getCurrentUser - `() {
        // given

        // when

        // then
    }
}
