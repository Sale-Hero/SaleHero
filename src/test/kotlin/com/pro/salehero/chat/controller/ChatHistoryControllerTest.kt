package com.pro.salehero.chat.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pro.salehero.chat.dto.ChatMessageDto
import com.pro.salehero.chat.service.ChatHistoryService
import com.pro.salehero.common.dto.PageResponseDTO
import com.pro.salehero.config.IntegrationControllerTestSupport
import com.pro.salehero.domain.chat.MessageType
import com.pro.salehero.util.exception.GlobalExceptionHandler
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(controllers = [ChatHistoryController::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler::class)
class ChatHistoryControllerTest : IntegrationControllerTestSupport() {

    @MockBean
    private lateinit var chatHistoryService: ChatHistoryService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `getChatHistory - 채팅 기록 조회 성공`() {
        // given
        val now = LocalDateTime.now()
        val chatMessage1 = ChatMessageDto(MessageType.CHAT, "user1", "Hello", now.minusMinutes(2))
        val chatMessage2 = ChatMessageDto(MessageType.CHAT, "user2", "Hi there", now.minusMinutes(1))
        val expectedContent = listOf(chatMessage2, chatMessage1)

        val expectedResult = PageResponseDTO(
            totalPages = 1,
            totalElement = 2,
            content = expectedContent
        )

        given(chatHistoryService.getChatHistory(any<Pageable>())).willReturn(expectedResult)

        // when & then
        mockMvc.perform(
            get("/api/chat/history")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.totalElement").value(2L))
    }
}
