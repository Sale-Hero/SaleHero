package com.pro.salehero.domain.subscribe.subscriber

import com.pro.salehero.domain.subscribe.subscriber.QSubscriber.subscriber
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class SubscribeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : SubscribeRepositoryCustom {
    override fun findByUserEmail(email: String): List<Subscriber> {
        return queryFactory
            .selectFrom(subscriber)
            .where(
                subscriber.userEmail.eq(email),
//                subscriber.isSubscribed.eq("Y")
            )
            .fetch()
    }

    override fun isThisUserSubscribed(email: String): Boolean {
        return queryFactory
            .selectFrom(subscriber)
            .where(
                subscriber.userEmail.eq(email),
                subscriber.isSubscribed.eq("Y")
            )
            .fetch()
            .isNotEmpty()
    }

    override fun unSubscribe(email: String) {
        queryFactory
            .update(subscriber)
            .set(subscriber.isSubscribed, "N")
            .where(subscriber.userEmail.eq(email))
            .execute()
    }

}
