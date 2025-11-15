package com.pro.salehero.common.service

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.dto.ViewCount
import com.pro.salehero.domain.newsletter.NewsLetterRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class ViewCountService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val newsLetterRepository: NewsLetterRepository
) {
    companion object {
        private const val VIEW_COUNT_KEY_PREFIX = "view_count:"
        private const val VIEWED_KEY_PREFIX = "viewed:"
        private const val USER_EXPIRATION_MINUTES = 5L
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     * 개별 조회수 조회 함수
     * Community / NewsLetter + id로 조회
     * 반환은 객체로 처리
     */
    fun getViewCount(
        contentType: RedisContentType,
        id: Long
    ): ViewCount {
        val viewCountKey = "${VIEW_COUNT_KEY_PREFIX}${contentType}:${id}"
        val count = redisTemplate.opsForValue().get(viewCountKey)?.toString()?.toLongOrNull() ?: 0L

        return ViewCount(
            type = contentType,
            id = id,
            viewCount = count
        )
    }

    /**
     * 타입별 조회수 조회 함수
     * Community / NewsLetter + id로 조회
     * 반환은 객체로 처리
     */
    fun getAllViewCounts(contentType: RedisContentType): List<ViewCount> {
        val pattern = "${VIEW_COUNT_KEY_PREFIX}${contentType}:*"
        val keys = redisTemplate.keys(pattern)

        return keys.mapNotNull { key ->
            val id = key.substringAfterLast(":").toLongOrNull()
            val count = redisTemplate.opsForValue().get(key)?.toString()?.toLongOrNull() ?: 0L
            id?.let {
                ViewCount(
                    type = contentType,
                    id = id,
                    viewCount = count
                )
            }
        }
    }

    /**
     * 조회수 증가 함수
     1. 사용자 식별정보로 이미 조회했는지 확인
     2. 1번이 없는경우 조회수 추가 & 사용자 식별정보 저장
     3. 1번이 있는 경우 continue
     */
    fun increaseViewCount(
        contentType: RedisContentType,
        id: Long,
        userIdentifier: String
    ) {
        logger.info("userIdentifier: $userIdentifier")
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
