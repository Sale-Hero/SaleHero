package com.pro.salehero.domain.subscribe.subscriber

interface SubscribeRepositoryCustom {
    fun findByUserEmail(email: String): List<Subscriber>

    fun isThisUserSubscribed(email: String): Boolean

    fun unSubscribe(email: String)
}
