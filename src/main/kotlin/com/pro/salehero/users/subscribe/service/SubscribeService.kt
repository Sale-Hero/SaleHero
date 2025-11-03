package com.pro.salehero.users.subscribe.service

import com.pro.salehero.common.dto.ResponseDTO
import com.pro.salehero.users.subscribe.dto.SubscribePostDTO
import com.pro.salehero.users.subscribe.dto.SubscriberResponseDTO
import com.pro.salehero.users.subscribe.dto.UnSubscribeDTO
import com.pro.salehero.domain.subscribe.subscriber.SubscribeRepository
import com.pro.salehero.domain.subscribe.subscriber.Subscriber
import com.pro.salehero.util.comfortutil.ComfortUtil
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscribeService(
    private val subscribeRepository: SubscribeRepository,
    private val unsubscribeTokenService: UnsubscribeTokenService,
    private val comfortUtil: ComfortUtil
) {

    @Transactional(readOnly = true)
    fun isEmailNotPresent(
        email: String
    ) = comfortUtil.validateEmail(email)
        .let {
            ResponseDTO(
                success = true,
                message = "",
                data = subscribeRepository.findByUserEmail(email).isEmpty()
            )
        }

    @Transactional
    fun addSubscriber(
        dto: SubscribePostDTO
    ): ResponseEntity<SubscriberResponseDTO> {
        val email = dto.userEmail.also { comfortUtil.validateEmail(it) }
        val existingSubscriber = subscribeRepository.findByUserEmail(email)

        val subscriber =
            if (existingSubscriber.isEmpty()) { // 새로 생성
                Subscriber(
                    userEmail = email,
                    isSubscribed = "Y",
                    isMarketingAgreed = if (dto.isMarketingAgreed) "Y" else "N"
                )
            } else { // 기존 것 업데이트
                existingSubscriber.first().apply {
                    update(dto.isMarketingAgreed)
                }
            }

        val savedSubscriber = subscribeRepository.save(subscriber)
        return ResponseEntity.ok(SubscriberResponseDTO.of(savedSubscriber))
    }

    @Transactional
    fun unSubscribe(
        unSubscribeDTO: UnSubscribeDTO
    ): ResponseDTO<String> {
        val result = unsubscribeTokenService.validateUnsubscribeToken(
            unSubscribeDTO.email,
            unSubscribeDTO.token
        )

        return if (result) {
            subscribeRepository.unSubscribe(unSubscribeDTO.email)
            ResponseDTO.success("구독이 취소되었습니다.")
        } else {
            ResponseDTO.error("일시적인 문제가 발생했습니다.\n잠시 후 다시 시도해주세요.")
        }
    }
}
