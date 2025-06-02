package com.pro.salehero.users.community.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.users.community.controller.dto.CommunityPostDTO
import com.pro.salehero.users.community.domain.enums.CommunityCategory
import com.pro.salehero.users.community.service.CommunityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
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

        mockMvc.perform(
            post("/api/community")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(communityDTO))
        )
            .andExpect(status().isOk)


        // when

        // then
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