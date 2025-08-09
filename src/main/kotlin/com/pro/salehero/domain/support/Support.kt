package com.pro.salehero.domain.support

import com.pro.salehero.util.CreateAndUpdateAudit
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Support(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String,
    val content: String,
    val name: String,
    val email: String,
    val cellPhone: String,


): CreateAndUpdateAudit()
