package com.pro.salehero.users.subscribe.domain.subscriber

import org.springframework.data.jpa.repository.JpaRepository

interface SubscribeRepository: JpaRepository<Subscriber, Long>, SubscribeRepositoryCustom