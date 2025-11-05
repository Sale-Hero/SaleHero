/*
import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.config.IntegrationControllerTestSupport
import com.pro.salehero.users.subscribe.service.SubscribeService
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(controllers = [SubscribeController::class])
class SubscribeControllerTest: IntegrationControllerTestSupport() {

    @MockBean
    private lateinit var subscribeService: SubscribeService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `validateSubscriber - 구독 가능한 메일인지 조회 성공`() {
        // given
        val email = "test@example.com"

        given(subscribeService.isEmailNotPresent(email)).willReturn(ResponseDTO.success("구독 가능한 이메일입니다.", true))

        // when  // then
        mockMvc.perform(
            get("/api/subscribe/${email}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }
}
*/
