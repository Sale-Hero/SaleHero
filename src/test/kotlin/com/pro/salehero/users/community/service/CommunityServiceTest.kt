package com.pro.salehero.users.community.service

import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.users.community.domain.Community
import com.pro.salehero.users.community.domain.CommunityRepository
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.UserRepository
import com.pro.salehero.users.user.domain.enums.UserRole
import com.pro.salehero.util.exception.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.security.test.context.support.WithMockUser
import kotlin.test.Test

@SpringBootTest
class CommunityServiceTest {

    @Autowired
    private lateinit var communityRepository: CommunityRepository

    @Autowired
    private lateinit var communityService: CommunityService

    @Autowired
    private lateinit var userRepository: UserRepository

    @AfterEach
    fun tearDown() {
        communityRepository.deleteAllInBatch()
    }

    @Test
    @WithMockUser(username = "test@test.com")
    fun `createArticle - 성공`() {
        // given
        val user = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)

        val communityPostDTO = createCommunityDTO("테스트 제목", "테스트 내용")

        // when
        val result = communityService.createArticleWithUser(user, communityPostDTO)

        // then
        assertThat(result.title).isEqualTo("테스트 제목")
    }

    @Test
    fun `createArticle - 제목이 빈 문자인 경우 실패`() {
        // given
        val user = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)

        val communityPostDTO = createCommunityDTO(" ", "테스트 내용")

        // when, then
        assertThrows<CustomException> {
            communityService.createArticleWithUser(user, communityPostDTO)

        }
    }

    @Test
    fun `getArticles - 전체 조회 성공`() {
        // given
        val userDTO = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)
        val user = userRepository.save(userDTO)

        createCommunity("타이틀 1", "내용 1", user)
        createCommunity("타이틀 2", "내용 2", user)

        val searchDTO = CommunitySearchDTO(category = CommunityCategory.COMMUNITY)
        val pageable = PageRequest.of(0, 15)

        // when
        val result = communityService.getArticles(searchDTO, pageable)

        // then
        assertThat(result.content).hasSize(2)
            .extracting("content").containsOnly("내용 1", "내용 2")
    }

    @Test
    fun `getArticles - 전체 빈 결과 조회`() {
        // given
        val searchDTO = CommunitySearchDTO(category = CommunityCategory.COMMUNITY)
        val pageable = PageRequest.of(0, 15)

        // when
        val result = communityService.getArticles(searchDTO, pageable)

        // then
        assertThat(result.content).isEmpty()
    }

    @Test
    fun `getArticles - 페이지를 초과하는 결과 조회`() {
        // given
        val userDTO = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)
        val user = userRepository.save(userDTO)

        createCommunity("타이틀 1", "내용 1", user)
        createCommunity("타이틀 2", "내용 2", user)

        val searchDTO = CommunitySearchDTO(category = CommunityCategory.COMMUNITY)
        val pageable = PageRequest.of(3, 15)

        // when
        val result = communityService.getArticles(searchDTO, pageable)

        // then
        assertThat(result.content).isEmpty()
    }

    private fun createCommunityDTO(
        title: String,
        content: String,
    ) = CommunityPostDTO(
        title = title,
        content = content,
        category = CommunityCategory.COMMUNITY
    )

    private fun createCommunity(
        title: String,
        content: String,
        user: User
    ) = Community(
        title = title,
        content = content,
        category = CommunityCategory.COMMUNITY,
        writerId = user.id!!,
        viewCount = 0,
        isDeleted = "N"
    )
        .also { communityRepository.save(it) }


}