package com.pro.salehero.common.service

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.users.newsletter.domain.NewsLetterRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ViewCountService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val newsLetterRepository: NewsLetterRepository
) {
    companion object {
        private const val VIEW_COUNT_KEY_PREFIX = "view_count:"
        private const val VIEWED_KEY_PREFIX = "viewed:"
        private const val USER_EXPIRATION_MINUTES = 5L
    }

    fun increaseViewCount(
        contentType: RedisContentType,
        id: Long,
        userIdentifier: String
    ) {
        println("userIdentifier = ${userIdentifier}")
        val viewedKey = "${VIEWED_KEY_PREFIX}${contentType}:${id}:${userIdentifier}"
        val viewCountKey = "${VIEW_COUNT_KEY_PREFIX}${contentType}:${id}"

        // 1. userIdentifier로 5분짜리 임시 유저정보 확인
        val hasViewed = redisTemplate.hasKey(viewedKey)

        if (!hasViewed) {
            // 2. Redis에서 조회수 증가
            redisTemplate.opsForValue().increment(viewCountKey)

            // 5분 만료 유저정보 저장
            redisTemplate.opsForValue().set(
                viewedKey,
                "1",
                Duration.ofMinutes(USER_EXPIRATION_MINUTES)
            )
        }
    }
}