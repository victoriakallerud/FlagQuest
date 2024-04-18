package com.flagquest.game.models

interface AuthHandler {
    fun signIn(email: String, password: String, callback: (Boolean, String?) -> Unit)
    fun signUp(email: String, password: String, callback: (Boolean, String?) -> Unit)
    fun signOut()
}
