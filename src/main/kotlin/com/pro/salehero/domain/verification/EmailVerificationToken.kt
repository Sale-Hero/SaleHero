package com.pro.salehero.domain.verification

import com.pro.salehero.util.CreateAudit
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_verification_token")
class EmailVerificationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val tokenHash: String,

    @Column(nullable = false)
    val rawNewsLetterId: Long,

    @Column(nullable = false)
    val expiryDate: LocalDateTime,

) : CreateAudit()
