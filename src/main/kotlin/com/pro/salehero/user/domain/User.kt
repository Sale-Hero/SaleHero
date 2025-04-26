package com.pro.salehero.user.domain

import com.pro.salehero.util.CreateAndUpdateAudit
import com.pro.salehero.user.domain.enums.UserRole
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val userEmail: String,

    @Column(nullable = false, length = 50)
    val userName: String,

    @Column(nullable = false, length = 10)
    val nickName: String,

    @Column(nullable = true, length = 1)
    val isActive: String = "Y",

    @Enumerated(EnumType.STRING)
    val role: UserRole,
) : CreateAndUpdateAudit()