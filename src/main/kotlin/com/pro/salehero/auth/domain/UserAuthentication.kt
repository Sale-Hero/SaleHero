package com.pro.salehero.auth.domain

import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.*

@Entity
class UserAuthentication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val email: String,
    @Column(nullable = false, length = 8)
    var code: String,
) : CreateAndUpdateAudit()
