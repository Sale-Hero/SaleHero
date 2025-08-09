package com.pro.salehero.domain.auth

import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthenticationRepository: JpaRepository<UserAuthentication, String>{
    fun findByEmail(email: String): UserAuthentication?
}