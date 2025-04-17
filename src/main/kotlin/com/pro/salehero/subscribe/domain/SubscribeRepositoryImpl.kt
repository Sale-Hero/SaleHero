package com.pro.salehero.subscribe.domain

import com.pro.salehero.subscribe.domain.QSubscriber.subscriber
import com.querydsl.jpa.impl.JPAQueryFactory

class SubscribeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SubscribeRepositoryCustom {
    override fun findByUserEmail(email: String): List<Subscriber> {
        return queryFactory
            .selectFrom(subscriber)
            .where(
                subscriber.userEmail.eq(email),
                subscriber.isSubscribed.eq("Y")
            )
            .fetch()
    }

}