package com.pro.salehero.users.community.domain

import com.pro.salehero.config.IntegrationTestSupport
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.UserRepository
import com.pro.salehero.users.user.domain.enums.UserRole
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@Transactional
class CommunityRepositoryImplTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var communityRepository: CommunityRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @AfterEach
    fun tearDown() {
        communityRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
    }

    @Test
    fun `getArticles - 저장된 게시물들을 조회한다`() {
        // given
        val user = createUser()
        val community1 = createCommunity("제목 1", "내용 1", user)
        val community2 = createCommunity("제목 2", "내용 2", user)
        val community3 = createCommunity("제목 3", "내용 3", user)

        communityRepository.saveAll(listOf(community1, community2, community3))

        val searchDTO = CommunitySearchDTO(category = CommunityCategory.COMMUNITY)
        val pageable = PageRequest.of(0, 15)

        // when
        val result = communityRepository.getArticles(searchDTO, pageable)

        // then
        assertThat(result.content).hasSize(3)
            .extracting("title","content","writerName")
            .containsExactlyInAnyOrder(
                tuple("제목 1", "내용 1", "허허"),
                tuple("제목 2", "내용 2", "허허"),
                tuple("제목 3", "내용 3", "허허")
            )
    }

    private fun createUser(): User {
        val userDTO = User(1L, "test@test.com", "test", "허허", "Y", UserRole.USER)
        val user = userRepository.save(userDTO)
        return user
    }

    private fun createCommunity(
        title: String,
        content: String,
        user: User
    ): Community = Community(
        title = title,
        content = content,
        category = CommunityCategory.COMMUNITY,
        writerId =  user.id!!,
        viewCount =  0,
        isDeleted = "N"
    )
}