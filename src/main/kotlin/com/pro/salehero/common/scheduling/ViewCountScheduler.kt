package com.pro.salehero.common.scheduling

import com.pro.salehero.common.enums.RedisContentType
import com.pro.salehero.common.service.ViewCountService
import com.pro.salehero.users.community.service.CommunityService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ViewCountScheduler(
    private val viewCountService: ViewCountService,
    private val communityService: CommunityService,
) {
//    @Scheduled(cron = "0/10 * * * * *", zone = "Asia/Seoul")
    fun updateCommunityViewCount() {
         viewCountService.getAllViewCounts(RedisContentType.COMMUNITY)
            .forEach { communityService.updateViewCount(it) }
             .also { println("업데이트 완료") }
    }
}