package com.pro.salehero.subscribe.domain.subscriber

import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
data class Subscriber(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val userEmail: String,

    @Column(nullable = true, length = 1)
    val isSubscribed: String = "Y",

    @Column(nullable = true, length = 1)
    val isMarketingAgreed: String = "N",

) : CreateAndUpdateAudit()