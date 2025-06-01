package com.pro.salehero.users.community.service

import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.UserRepository
import com.pro.salehero.users.user.domain.enums.UserRole
import com.pro.salehero.util.exception.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
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
        val user = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)

        val communityPostDTO = createCommuinity("테스트 제목", "테스트 내용")

        // when
        val result = communityService.createArticleWithUser(user, communityPostDTO)

        // then
        assertThat(result.title).isEqualTo("테스트 제목")
    }

    @Test
    fun `createArticle - 제목이 빈 문자인 경우 실패`() {
        // given
        val user = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)

        val communityPostDTO = createCommuinity(" ", "테스트 내용")

        // when, then
        assertThrows<CustomException> {
            communityService.createArticleWithUser(user, communityPostDTO)

        }
    }

    private fun createCommuinity(
        title: String ,
        content: String ,
    ) = CommunityPostDTO(
        title = title,
        content = content,
        category = CommunityCategory.COMMUNITY
    )


}