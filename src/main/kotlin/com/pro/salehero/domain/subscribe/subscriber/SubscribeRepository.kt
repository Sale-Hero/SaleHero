package com.pro.salehero.domain.subscribe.subscriber

import org.springframework.data.jpa.repository.JpaRepository

interface SubscribeRepository: JpaRepository<Subscriber, Long>, SubscribeRepositoryCustom