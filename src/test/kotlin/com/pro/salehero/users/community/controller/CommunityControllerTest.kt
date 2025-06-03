package com.pro.salehero.users.community.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.users.community.controller.dto.CommunitySearchDTO
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.community.service.CommunityService
import com.pro.salehero.util.exception.GlobalExceptionHandler
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import kotlin.test.Test

@WebMvcTest(controllers = [CommunityController::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler::class)
class CommunityControllerTest {

    @MockBean
    private lateinit var communityService: CommunityService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    fun `postArticle - 게시글 작성 성공`() {
        // given
        val communityDTO = createCommunityDTO("제목1", "내용 1")

        // when // then
        mockMvc.perform(
            post("/api/community")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(communityDTO))
        )
            .andExpect(status().isOk)

        verify(communityService).createArticle(communityDTO)
    }

    @Test
    fun `postArticle - 필수 정보 누락으로 인한 작성 실패`() {
        // given
        val communityDTO = createCommunityDTO(" ", "내용 1")

        // when // when
        mockMvc.perform(
            post("/api/community")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(communityDTO))
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$.bindingError.errorMsg").value("제목은 필수입니다."))
    }

    @Test
    fun `getArticles - 커뮤니티 리스트 조회`() {
        // given
        val expectedResult = PageResponseDTO<CommunityResponseDTO>(0, 0, emptyList())
        val requestDTO = CommunitySearchDTO(category = null)
        val pageable: Pageable = PageRequest.of(0, 10)
        given(communityService.getArticles(requestDTO, pageable)).willReturn(expectedResult)

        // when // then
        mockMvc.perform(
            get("/api/community")
        )
            .andDo(print())
            .andExpect(status().isOk)
    }

    private fun createCommunityDTO(
        title: String,
        content: String,
    ) = CommunityPostDTO(
        title,
        content,
        CommunityCategory.COMMUNITY
    )
}