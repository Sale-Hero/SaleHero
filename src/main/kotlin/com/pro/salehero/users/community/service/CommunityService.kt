package com.pro.salehero.users.community.service

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.common.service.dto.ViewCount
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.users.community.domain.Community
import com.pro.salehero.users.community.domain.CommunityRepository
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.util.comfortutil.ComfortUtil
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommunityService(
    private val communityRepository: CommunityRepository,
    private val viewCountService: ViewCountService,
    private val comfortUtil: ComfortUtil,
) {
    fun createArticle(
        communityPostDTO: CommunityPostDTO
    ): CommunityResponseDTO = getCurrentUser()
        .let { createArticleWithUser(it, communityPostDTO) }

    fun createArticleWithUser(
        user: User,
        communityPostDTO: CommunityPostDTO
    ): CommunityResponseDTO =
        validatePostDTO(communityPostDTO)
            .let {
                Community(
                    title = communityPostDTO.title,
                    content = communityPostDTO.content,
                    category = communityPostDTO.category,
                    writerId = user.id!!,
                    viewCount = 0,
                    isDeleted = "N"
                )
            }
            .let { communityRepository.save(it) }
            .let {
                CommunityResponseDTO(
                    it.id!!,
                    it.title,
                    it.content,
                    it.createdAt,
                    it.viewCount,
                    user.nickName
                )
            }


    @Transactional(readOnly = true)
    fun getArticles(
        dto: CommunitySearchDTO,
        pageable: Pageable
    ) = communityRepository.getArticles(dto, pageable)

    @Transactional(readOnly = true)
    fun getArticle(
        id: Long,
        request: HttpServletRequest
    ): CommunityResponseDTO {
        val article = communityRepository.findById(id)
        if (article.isEmpty) {
            throw CustomException(ErrorCode.CODE_404)
        }
        if (article.get().isDeleted == "Y") {
            throw CustomException(ErrorCode.CODE_4042)
        }

        viewCountService.increaseViewCount(
            RedisContentType.COMMUNITY,
            article.get().id!!,
            comfortUtil.getUserIdentifier(request)
        )

        return CommunityResponseDTO.of(article.get())
    }

    fun updateViewCount(viewCount: ViewCount) = communityRepository.updateViewCount(viewCount)

    private fun validatePostDTO(communityPostDTO: CommunityPostDTO) {
        require(communityPostDTO.title.isNotEmpty()) { throw CustomException(ErrorCode.CODE_4043) }
        require(communityPostDTO.title.isNotBlank()) { throw CustomException(ErrorCode.CODE_4043) }
        require(communityPostDTO.content.isNotEmpty()) { throw CustomException(ErrorCode.CODE_4043) }
        require(communityPostDTO.content.isNotBlank()) { throw CustomException(ErrorCode.CODE_4043) }
    }

}
