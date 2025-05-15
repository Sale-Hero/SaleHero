package com.pro.salehero.users.subscribe.domain.subscriber

interface SubscribeRepositoryCustom {
    fun findByUserEmail(email: String): List<Subscriber>
}