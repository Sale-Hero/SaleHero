package com.pro.salehero.subscribe.service

import com.pro.salehero.common.ResponseDTO
import com.pro.salehero.subscribe.controller.dto.SubscribePostDTO
import com.pro.salehero.subscribe.controller.dto.SubscriberResponseDTO
import com.pro.salehero.subscribe.domain.SubscribeRepository
import com.pro.salehero.subscribe.domain.Subscriber
import com.pro.salehero.util.comfortutil.ComfortUtil
import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscribeService(
    val subscribeRepository: SubscribeRepository,
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
    ): ResponseEntity<SubscriberResponseDTO> =
        dto.userEmail
            .also { comfortUtil.validateEmail(it) }
            .also { println("dto.isMarketingAgreed = ${dto.isMarketingAgreed}") }
            .takeIf { subscribeRepository.findByUserEmail(it).isEmpty() }
            ?.let {
                Subscriber(
                    isSubscribed = "Y",
                    userEmail = it,
                    frequency = dto.frequency,
                    isMarketingAgreed = if (dto.isMarketingAgreed) "Y" else "N",
                )
            }
            ?.let(subscribeRepository::save)
            ?.let(SubscriberResponseDTO::of)
            ?.let { ResponseEntity.ok(it) }
            ?: throw CustomException(ErrorCode.CODE_4001)
}