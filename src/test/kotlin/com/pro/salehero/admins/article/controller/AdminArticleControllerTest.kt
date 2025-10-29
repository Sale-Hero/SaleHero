package com.pro.salehero.admins.article.controller

import com.pro.salehero.admins.article.service.AdminArticleService
import com.pro.salehero.domain.article.Article
import com.pro.salehero.domain.article.dto.AdminArticleDTO
import com.pro.salehero.domain.article.dto.AdminArticleDeleteDTO
import com.pro.salehero.domain.article.dto.AdminArticlePostDTO
import com.pro.salehero.common.enums.ContentsCategory
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AdminArticleControllerTest {

    @InjectMocks
    private lateinit var adminArticleController: AdminArticleController

    @Mock
    private lateinit var adminArticleService: AdminArticleService

    @Test
    fun `createAdminArticle - 관리자 글 생성 성공`() {
        // given
        val dto = AdminArticlePostDTO(
            title = "테스트 글",
            content = "테스트 내용",
            category = ContentsCategory.PROMOTION,
            summary = "요약",
            isVisible = "Y"
        )
        val article = Article(
            title = "테스트 글",
            content = "테스트 내용",
            category = ContentsCategory.PROMOTION,
            summary = "요약",
            isVisible = "Y"
        )

        given(adminArticleService.createAdminArticle(dto))
            .willReturn(article)

        // when
        val result = adminArticleController.createAdminArticle(dto)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(article)
    }

    @Test
    fun `approveArticle - 승인 성공`() {
        // given
        val rawNewsLetterId = 1L
        val expectedResponse = AdminArticleDTO(
            id = 1L,
            title = "승인된 글",
            content = "승인된 내용",
            category = ContentsCategory.PROMOTION,
            summary = "",
            isVisible = "Y",
            isDeleted = "N",
            createdAt = LocalDateTime.now(),
            viewCount = 0L
        )

        given(adminArticleService.approveArticleFromRawNewsLetter(rawNewsLetterId))
            .willReturn(expectedResponse)

        // when
        val result = adminArticleController.approveArticle(rawNewsLetterId)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(expectedResponse)
    }

    @Test
    fun `approveArticle - 존재하지 않는 RawNewsLetter ID로 승인 실패`() {
        // given
        val invalidId = 999L

        given(adminArticleService.approveArticleFromRawNewsLetter(invalidId))
            .willAnswer { throw CustomException(ErrorCode.CODE_404) }

        // when & then
        assertThatThrownBy {
            adminArticleController.approveArticle(invalidId)
        }.isInstanceOf(CustomException::class.java)
    }

    @Test
    fun `modifyArticle - 글 수정 성공`() {
        // given
        val articleId = 1L
        val dto = AdminArticlePostDTO(
            title = "수정된 글",
            content = "수정된 내용",
            category = ContentsCategory.PROMOTION,
            summary = "수정된 요약",
            isVisible = "Y"
        )
        val expectedResponse = AdminArticleDTO(
            id = articleId,
            title = "수정된 글",
            content = "수정된 내용",
            category = ContentsCategory.PROMOTION,
            summary = "수정된 요약",
            isVisible = "Y",
            isDeleted = "N",
            createdAt = LocalDateTime.now(),
            viewCount = 10L
        )

        given(adminArticleService.modifyAdminArticle(articleId, dto))
            .willReturn(expectedResponse)

        // when
        val result = adminArticleController.modifyArticle(articleId, dto)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(expectedResponse)
    }

    @Test
    fun `deleteArticle - 글 삭제 성공`() {
        // given
        val dto = AdminArticleDeleteDTO(listOf(1L, 2L))
        val mockResult = emptyList<Article>()

        given(adminArticleService.deleteArticle(dto))
            .willReturn(mockResult)

        // when
        val result = adminArticleController.deleteArticle(dto)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}