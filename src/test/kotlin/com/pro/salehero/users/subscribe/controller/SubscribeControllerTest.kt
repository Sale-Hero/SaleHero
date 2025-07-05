package com.pro.salehero.users.subscribe.controller

import com.pro.salehero.users.subscribe.service.SubscribeService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class SubscribeControllerTest {

    @Mock
    private lateinit var subscribeController: SubscribeController

    @InjectMocks
    private lateinit var subscribeService: SubscribeService

    @Test
    fun `validateSubscriber - 구독 가능한 메일인지 조회 성공`() {
        // given

        // when

        // then
    }
}
