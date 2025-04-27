package com.pro.salehero.subscribe.domain.subscriber

interface SubscribeRepositoryCustom {
    fun findByUserEmail(email: String): List<Subscriber>
}