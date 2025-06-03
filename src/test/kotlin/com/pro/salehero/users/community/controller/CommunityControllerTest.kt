package com.pro.salehero.users.community.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.controller.dto.CommunityResponseDTO
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.community.service.CommunityService
import com.pro.salehero.users.user.domain.User
import com.pro.salehero.users.user.domain.enums.UserRole
import com.pro.salehero.util.security.SecurityUtil
import com.pro.salehero.util.security.SecurityUtil.Companion.getCurrentUser
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import kotlin.test.Test

@WebMvcTest(controllers = [CommunityController::class])
@AutoConfigureMockMvc(addFilters = false)
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
        val expectedResult = CommunityResponseDTO(
            id = 1L,
            title = "제목1",
            content ="내용 1",
            createdAt = LocalDateTime.now(),
            viewCount = 0L,
            writerName = "허허"
        )

        given(communityService.createArticle(communityDTO)).willReturn(expectedResult)

        // when // then
        mockMvc.perform(
            post("/api/community")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(communityDTO))
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("제목1"))
            .andExpect(jsonPath("$.content").value("내용 1"))

        verify(communityService).createArticle(communityDTO)
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