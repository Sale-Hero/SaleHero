package com.pro.salehero.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthenticationRepository: JpaRepository<UserAuthentication, String>{
    fun findByEmail(email: String): UserAuthentication?
}