package com.pro.salehero.users.subscribe.controller

import com.pro.salehero.users.subscribe.controller.dto.SubscribePostDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscribe")
class SubscribeController (
    val subscribeService: com.pro.salehero.users.subscribe.service.SubscribeService
){

    @GetMapping("/{email}")
    fun validateSubscriber(
        @PathVariable("email") email: String
    ) = subscribeService.isEmailNotPresent(email)

    @PostMapping
    fun addSubscriber(
        @RequestBody dto: SubscribePostDTO
    ) = subscribeService.addSubscriber(dto)

    @PostMapping("/unsubscribe/{email}")
    fun unSubscribe(
        @PathVariable("email") email: String
    ) = subscribeService.unSubscribe(email)
}
