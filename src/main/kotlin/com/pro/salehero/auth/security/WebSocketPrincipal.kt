package com.pro.salehero.auth.security

import java.security.Principal

/**
 * Represents a user in the WebSocket session.
 * Even for anonymous users, this principal provides a unique identifier
 * that Spring can use to target messages to a specific session.
 */
data class WebSocketPrincipal(
    private val name: String
) : Principal {
    override fun getName(): String = name
}
