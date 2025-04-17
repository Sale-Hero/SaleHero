package com.pro.salehero.subscribe.domain

interface SubscribeRepositoryCustom {
    fun findByUserEmail(email: String): List<Subscriber>
}