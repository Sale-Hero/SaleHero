package com.pro.salehero.common.scheduling

import com.pro.salehero.admins.article.service.AdminArticleService
import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.config.SecurityDebugFilter
import com.pro.salehero.users.community.service.CommunityService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ViewCountScheduler(
    private val viewCountService: ViewCountService,
    private val communityService: CommunityService,
    private val adminArticleService: AdminArticleService,
) {
    private val log = LoggerFactory.getLogger(SecurityDebugFilter::class.java)

    @Scheduled(cron = "1 * * * * *", zone = "Asia/Seoul")
    fun updateCommunityViewCount() {
         viewCountService.getAllViewCounts(RedisContentType.COMMUNITY)
            .forEach { communityService.updateViewCount(it) }
             .also { log.info("커뮤니티 업데이트 완료") }

        viewCountService.getAllViewCounts(RedisContentType.ARTICLE)
            .forEach{ adminArticleService.updateViewCount(it)}
            .also { log.info("아티클 업데이트 완료") }
    }
}
