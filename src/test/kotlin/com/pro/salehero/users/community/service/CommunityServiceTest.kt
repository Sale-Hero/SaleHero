package com.pro.salehero.users.community.service

import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.domain.Community
import com.pro.salehero.users.community.domain.CommunityRepository
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.UserRepository
import com.pro.salehero.users.user.domain.enums.UserRole
import com.pro.salehero.users.user.service.UserService
import com.pro.salehero.util.comfortutil.ComfortUtil
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import kotlin.test.Test

@SpringBootTest
class CommunityServiceTest {

    @Autowired
    private lateinit var communityService: CommunityService

    @Autowired
    private lateinit var userRepository: UserRepository


    @Test
    @WithMockUser(username = "test@test.com")
    fun `createArticle - 성공`() {
        // given
        val user = User(
            1L, "test@test.com", "test", "test","Y", UserRole.USER
        )
        userRepository.save(user)

        val communityPostDTO = CommunityPostDTO(
            title = "테스트 제목",
            content = "테스트 내용",
            category = CommunityCategory.COMMUNITY
        )

        // when
        val result = communityService.createArticle(communityPostDTO)

        // then
        assertThat(result.title).isEqualTo("테스트 제목")
    }
}