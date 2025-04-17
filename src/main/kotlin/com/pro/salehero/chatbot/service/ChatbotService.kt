package com.pro.salehero.chatbot.service

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.pro.salehero.chatbot.doomain.ChatHistory
import com.pro.salehero.chatbot.doomain.ChatHistoryRepository
import com.pro.salehero.chatbot.doomain.ChatbotConversation
import com.pro.salehero.chatbot.doomain.ChatbotConversationRepository
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatbotService(
    @Value("\${openai.api.key}")
    final val openAIApiKey: String,

    @Value("\${openai.api.model}")
    final val model: String,

    @Value("\${openai.api.chatcontent}")
    val content: String,
    val chatHistoryRepository: ChatHistoryRepository,
    val chatbotConversationRepository: ChatbotConversationRepository
) {
    private val openAI = OpenAI(openAIApiKey)

    suspend fun askSomeThing(
        question: String,
        request: HttpServletRequest
    ): String {
        val sessionId = request.getSession(false)?.id
        val ip = request.remoteAddr

        val answer = askQuestion(question)
        val existingChatHistory = isHistoryExist(sessionId, ip)

        // 대화 기록이 있으면 기존 기록 사용, 없으면 새로 생성
        val chatHistory = existingChatHistory ?: run {
            val newHistory = ChatHistory(
                ip = ip,
                referer = request.getHeader("Referer"),
                userAgent = request.getHeader("User-Agent"),
                sessionId = sessionId
            )
            chatHistoryRepository.save(newHistory)
        }

        // 대화 내용 저장
        val conversation = ChatbotConversation(
            question = question,
            answer = answer,
            chatHistoryIdx = chatHistory.id!!
        )

        withContext(Dispatchers.IO) {
            chatbotConversationRepository.save(conversation)
        }

        return answer
    }

    // open ai 문답 함수
    @OptIn(BetaOpenAI::class)
    private suspend fun askQuestion(
        question: String,
    ) = openAI.chatCompletion(
        ChatCompletionRequest(
            model = ModelId(model),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = content,
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = question
                )
            ),
            temperature = 0.7,
            maxTokens = 1000
        )
    )
        .let { it.choices.first().message?.content ?: "죄송합니다, 답변을 생성할 수 없습니다." }


    // 이전에 채팅 히스토리 있는지
    private fun isHistoryExist(
        sessionId: String?,
        ip: String,
    ) = if (sessionId != null) {
        chatHistoryRepository.findBySessionIdAndCreatedAtAfter(
            sessionId,
            LocalDateTime.now().minusMinutes(30)
        )
    } else {
        chatHistoryRepository.findByIpAndCreatedAtAfter(
            ip,
            LocalDateTime.now().minusMinutes(30)
        )
    }
}

