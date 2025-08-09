package com.pro.salehero.users.community.service

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.common.service.dto.ViewCount
import com.pro.salehero.config.IntegrationTestSupport
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.domain.community.Community
import com.pro.salehero.domain.community.CommunityRepository
import com.pro.salehero.domain.community.enums.CommunityCategory
import com.pro.salehero.domain.user.User
import com.pro.salehero.domain.user.UserRepository
import com.pro.salehero.domain.user.enums.UserRole
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

@MockBean(ViewCountService::class)
class CommunityServiceTest: IntegrationTestSupport() {

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

    @Test
    fun `getArticle - 삭제된 데이터 조회`() {
        // given
        val user = createUser()
        val community = Community(
            title = "제목 1",
            content = "내용 1",
            category = CommunityCategory.COMMUNITY,
            writerId = user.id!!,
            viewCount = 0,
            isDeleted = "Y"
        )

        val createdCommunity = communityRepository.save(community)
        val mockRequest = MockHttpServletRequest()
        mockRequest.remoteAddr = "127.0.0.1"

        // when // then
        assertThrows<CustomException> { communityService.getArticle(createdCommunity.id!!, request = mockRequest) }
    }

    @Test
    fun `updateViewCount - 조회수 증가 성공`() {
        // given
        val user = createUser()
        val community = createCommunity("제목 12", "내용 12", user)
        val initialViewCount = community.viewCount

        val increasedViewCount = 10L

        val viewCount = ViewCount(
            type = RedisContentType.COMMUNITY,
            id = community.id!!,
            viewCount = increasedViewCount
            )

        // when
        communityService.updateViewCount(viewCount)
        val result = communityRepository.findById(community.id!!)

        // then
        assertThat(result.get().viewCount).isEqualTo(initialViewCount + increasedViewCount)
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