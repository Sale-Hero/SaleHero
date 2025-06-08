package com.pro.salehero.users.community.domain

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.dto.ViewCount
import com.pro.salehero.config.IntegrationTestSupport
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.UserRepository
import com.pro.salehero.users.user.domain.enums.UserRole
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@Transactional
class CommunityRepositoryImplTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var communityRepository: CommunityRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @AfterEach
    fun tearDown() {
        communityRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
    }

    @Test
    fun `getArticles - 저장된 게시물들을 조회한다`() {
        // given
        val user = createUser()
        val community1 = createCommunity("제목 1", "내용 1", user, CommunityCategory.COMMUNITY)
        val community2 = createCommunity("제목 2", "내용 2", user, CommunityCategory.COMMUNITY)
        val community3 = createCommunity("제목 3", "내용 3", user, CommunityCategory.COMMUNITY)

        communityRepository.saveAll(listOf(community1, community2, community3))

        val searchDTO = CommunitySearchDTO(null)
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

    @Test
    fun `getArticles - 카테고리별 조회 테스트`() {
        // given
        val user = createUser()
        val communityPost1 = createCommunity("제목 1", "내용1", user, CommunityCategory.COMMUNITY)
        val infomPost1 = createCommunity("제목 2", "내용2", user, CommunityCategory.INFORMATION)
        val communityPost2 = createCommunity("제목 3", "내용3", user, CommunityCategory.COMMUNITY)
        val infomPost2 = createCommunity("제목 4", "내용4", user, CommunityCategory.INFORMATION)

        communityRepository.saveAll(listOf(communityPost1, communityPost2, infomPost1, infomPost2))

        val searchDTO = CommunitySearchDTO(CommunityCategory.COMMUNITY)
        val pageable = PageRequest.of(0, 15)

        // when
        val result = communityRepository.getArticles(searchDTO, pageable)

        // then
        assertThat(result.content).hasSize(2)
            .extracting("title","content","writerName")
            .containsExactlyInAnyOrder(
                tuple("제목 1", "내용1", "허허"),
                tuple("제목 3", "내용3", "허허"),
            )
    }

    @Test
    fun `updateViewCount - 조회수 업데이트 성공 테스트`() {
        // given
        val updatedViewCount = 10L
        val user = createUser()
        val community = createCommunity("조회수 테스트용 제목1"," 조회수 테스트용 내용1", user, CommunityCategory.COMMUNITY)
        val savedCommunity = communityRepository.save(community)

        val viewCount = ViewCount(RedisContentType.COMMUNITY, savedCommunity.id!!, updatedViewCount)

        // when
        communityRepository.updateViewCount(viewCount)
        entityManager.flush()
        entityManager.clear()
        val updatedCommunity = communityRepository.findById(savedCommunity.id!!)

        // then
        assertThat(updatedCommunity.get().viewCount).isEqualTo(updatedViewCount)
    }

    private fun createUser(): User {
        val userDTO = User(1L, "test@test.com", "test", "허허", "Y", UserRole.USER)
        val user = userRepository.save(userDTO)
        return user
    }

    private fun createCommunity(
        title: String,
        content: String,
        user: User,
        category: CommunityCategory
    ): Community = Community(
        title = title,
        content = content,
        category = category,
        writerId =  user.id!!,
        viewCount =  0,
        isDeleted = "N"
    )
}
