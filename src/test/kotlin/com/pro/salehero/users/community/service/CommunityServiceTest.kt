package com.pro.salehero.users.community.service

import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.domain.Community
import com.pro.salehero.users.community.domain.CommunityRepository
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.enums.UserRole
import com.pro.salehero.users.user.service.UserService
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
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class CommunityServiceTest {

    @Mock
    private lateinit var communityRepository: CommunityRepository

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var communityService: CommunityService

    @Mock
    private lateinit var viewCountService: ViewCountService


    @Test
    fun `createArticle - 성공`() {
        // given
        val mockUser = User(
            id = 1L,
            userEmail = "test@test.com",
            userName = "test",
            nickName = "test",
            isActive = "Y",
            role = UserRole.USER
        )
        val communityPostDTO = CommunityPostDTO(
            title = "테스트 제목",
            content = "테스트 내용",
            category = CommunityCategory.COMMUNITY
        )
        val savedCommunity = Community(
            id = 1L,
            title = "테스트 제목",
            content = "테스트 내용",
            category = CommunityCategory.COMMUNITY,
            writerId = 1L,
            viewCount = 0,
            isDeleted = "N"
        )

        `when`(getCurrentUser()).thenReturn(mockUser)
        `when`(communityRepository.save(any(Community::class.java))).thenReturn(savedCommunity)

        // when
        val result = communityService.createArticle(communityPostDTO)

        // then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.title).isEqualTo("테스트 제목")
        verify(communityRepository).save(any(Community::class.java))
        verify(getCurrentUser())
    }
}