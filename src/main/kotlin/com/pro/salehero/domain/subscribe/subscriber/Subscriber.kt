package com.pro.salehero.domain.subscribe.subscriber

import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class Subscriber(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val userEmail: String,

    @Column(nullable = true, length = 1)
    var isSubscribed: String = "Y",

    @Column(nullable = true, length = 1)
    var isMarketingAgreed: String = "N",

) : CreateAndUpdateAudit(){

    fun update(marketingAgreed: Boolean) {
        this.isSubscribed = "Y"
        this.isMarketingAgreed = if (marketingAgreed) "Y" else "N"
    }
}
