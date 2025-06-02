package com.pro.salehero.users.community.service

import com.pro.salehero.common.service.ViewCountService
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
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.test.context.support.WithMockUser
import kotlin.test.Test

@SpringBootTest
@MockBean(ViewCountService::class)
//@Import(TestRedisConfiguration::class)
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
        userRepository.deleteAllInBatch()
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
        val user = createUser()

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
        val user = createUser()

        createCommunity("타이틀 1", "내용 1", user)
        createCommunity("타이틀 2", "내용 2", user)

        val searchDTO = CommunitySearchDTO(category = CommunityCategory.COMMUNITY)
        val pageable = PageRequest.of(3, 15)

        // when
        val result = communityService.getArticles(searchDTO, pageable)

        // then
        assertThat(result.content).isEmpty()
    }


    @Test
    fun `getArticle - 상세 조회 성공`() {
        // given
        val title = "제목 1"
        val content = "내용 1"

        val user = createUser()
        val post = createCommunity(title, content, user)
        val postResult = communityRepository.save(post)

        val mockRequest = MockHttpServletRequest()
        mockRequest.remoteAddr = "127.0.0.1"

        // when
        val result = communityService.getArticle(postResult.id!!, mockRequest)

        // then
        assertThat(result.title).isEqualTo(title)
    }

    @Test
    fun `getArticle - 존재하지 않는 상세 조회`() {
        // given
        val mockRequest = MockHttpServletRequest()
        mockRequest.remoteAddr = "127.0.0.1"

        // when // then
        assertThrows<CustomException> { communityService.getArticle(1L, mockRequest) }
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

    private fun createUser(): User {
        val userDTO = User(1L, "test@test.com", "test", "test", "Y", UserRole.USER)
        val user = userRepository.save(userDTO)
        return user
    }

}