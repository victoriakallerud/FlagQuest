package com.flagquest.game.models


interface AuthHandler {
    fun signIn(email: String, password: String, callback: (Boolean, String?, String?) -> Unit)
    fun signUp(email: String, password: String, callback: (Boolean, String?, String?) -> Unit)
    fun signOut() // TODO: implement logout
}
