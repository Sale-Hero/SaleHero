package com.pro.salehero.chatbot.controller

import com.pro.salehero.chatbot.service.ChatbotService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chatbot")
class ChatbotController(
    val chatbotService: ChatbotService
) {
    @GetMapping
    suspend fun answerQuestion(
        @RequestParam("query") question: String,
        request: HttpServletRequest
    ) = chatbotService.askSomeThing(question, request)

}