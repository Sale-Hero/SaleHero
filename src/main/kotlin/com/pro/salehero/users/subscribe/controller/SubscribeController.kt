package com.pro.salehero.users.subscribe.controller

import com.pro.salehero.users.subscribe.controller.dto.SubscribePostDTO
import com.pro.salehero.users.subscribe.controller.dto.UnSubscribeDTO
import com.pro.salehero.users.subscribe.service.SubscribeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscribe")
class SubscribeController (
    val subscribeService: SubscribeService
){

    @GetMapping("/{email}")
    fun validateSubscriber(
        @PathVariable("email") email: String
    ) = subscribeService.isEmailNotPresent(email)

    @PostMapping
    fun addSubscriber(
        @RequestBody dto: SubscribePostDTO
    ) = subscribeService.addSubscriber(dto)

    @GetMapping("/unsubscribe")
    fun unSubscribe(
        @RequestParam email: String,
        @RequestParam token: String
    ) = subscribeService.unSubscribe(UnSubscribeDTO(email, token))
}
