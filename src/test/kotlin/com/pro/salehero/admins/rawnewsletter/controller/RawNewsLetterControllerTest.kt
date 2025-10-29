//package com.pro.salehero.admins.rawnewsletter.controller
//
//import com.pro.salehero.admins.rawnewsletter.dto.RawNewsLetterDTO
//import com.pro.salehero.admins.rawnewsletter.dto.RawNewsLetterPostDTO
//import com.pro.salehero.admins.rawnewsletter.service.RawNewsLetterService
//import com.pro.salehero.common.dto.PageResponseDTO
//import com.pro.salehero.common.dto.ResponseDTO
//import com.pro.salehero.common.enums.ContentsCategory
//import com.pro.salehero.users.newsletter.dto.NewsLetterDeleteDTO
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.BDDMockito.given
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.junit.jupiter.MockitoExtension
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.domain.Sort
//
//@ExtendWith(MockitoExtension::class)
//class RawNewsLetterControllerTest {
//
//    @InjectMocks
//    private lateinit var rawNewsLetterController: RawNewsLetterController
//
//    @Mock
//    private lateinit var rawNewsLetterService: RawNewsLetterService
//
//    @Test
//    fun `getRawNewsLetters - 로우 뉴스레터 목록 조회 성공`() {
//        // given
//        val pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"))
//        val rawNewsLetterDTO = RawNewsLetterDTO(
//            id = 1L,
//            title = "테스트 뉴스레터",
//            content = "테스트 내용",
//            createdAt = "2024-01-01T10:00:00"
//        )
//        val expectedResponse = PageResponseDTO(
//            content = listOf(rawNewsLetterDTO),
//            totalPages = 1,
//            totalElement = 1L
//        )
//
//        given(rawNewsLetterService.getRawNewsLetters(pageable))
//            .willReturn(expectedResponse)
//
//        // when
//        val result = rawNewsLetterController.getRawNewsLetters(pageable)
//
//        // then
//        assertThat(result).isEqualTo(expectedResponse)
//        assertThat(result.content).hasSize(1)
//        assertThat(result.content[0].title).isEqualTo("테스트 뉴스레터")
//    }
//
//    @Test
//    fun `modifyRawNewsLetter - 로우 뉴스레터 수정 성공`() {
//        // given
//        val dto = RawNewsLetterPostDTO(
//            id = 1L,
//            title = "수정된 뉴스레터",
//            content = "수정된 내용",
//            category = ContentsCategory.PROMOTION,
//            articleUrl = "https://example.com",
//            keyword = "수정"
//        )
//        val expectedResponse = ResponseDTO.success("수정 완료", null)
//
//        given(rawNewsLetterService.modifyRawNewsLetter(dto))
//            .willReturn(expectedResponse)
//
//        // when
//        val result = rawNewsLetterController.modifyRawNewsLetter(dto)
//
//        // then
//        assertThat(result).isEqualTo(expectedResponse)
//        assertThat(result.success).isTrue()
//        assertThat(result.message).isEqualTo("수정 완료")
//    }
//
//    @Test
//    fun `modifyRawNewsLetter - 존재하지 않는 뉴스레터 수정 실패`() {
//        // given
//        val dto = RawNewsLetterPostDTO(
//            id = 999L,
//            title = "수정된 뉴스레터",
//            content = "수정된 내용",
//            category = ContentsCategory.PROMOTION,
//            articleUrl = "https://example.com",
//            keyword = "수정"
//        )
//        val expectedResponse = ResponseDTO.error("뉴스레터를 찾을 수 없습니다", null)
//
//        given(rawNewsLetterService.modifyRawNewsLetter(dto))
//            .willReturn(expectedResponse)
//
//        // when
//        val result = rawNewsLetterController.modifyRawNewsLetter(dto)
//
//        // then
//        assertThat(result).isEqualTo(expectedResponse)
//        assertThat(result.success).isFalse()
//        assertThat(result.message).isEqualTo("뉴스레터를 찾을 수 없습니다")
//    }
//
//    @Test
//    fun `deleteRawNewsLetter - 로우 뉴스레터 삭제 성공`() {
//        // given
//        val dto = NewsLetterDeleteDTO(listOf(1L, 2L))
//        val expectedResponse = ResponseDTO.success("삭제 완료", null)
//
//        given(rawNewsLetterService.deleteRawNewsLetter(dto))
//            .willReturn(expectedResponse)
//
//        // when
//        val result = rawNewsLetterController.deleteRawNewsLetter(dto)
//
//        // then
//        assertThat(result).isEqualTo(expectedResponse)
//        assertThat(result.success).isTrue()
//        assertThat(result.message).isEqualTo("삭제 완료")
//    }
//
//    @Test
//    fun `deleteRawNewsLetter - 빈 ID 목록으로 삭제 실패`() {
//        // given
//        val dto = NewsLetterDeleteDTO(emptyList())
//        val expectedResponse = ResponseDTO.error("삭제할 항목이 없습니다", null)
//
//        given(rawNewsLetterService.deleteRawNewsLetter(dto))
//            .willReturn(expectedResponse)
//
//        // when
//        val result = rawNewsLetterController.deleteRawNewsLetter(dto)
//
//        // then
//        assertThat(result).isEqualTo(expectedResponse)
//        assertThat(result.success).isFalse()
//        assertThat(result.message).isEqualTo("삭제할 항목이 없습니다")
//    }
//}
