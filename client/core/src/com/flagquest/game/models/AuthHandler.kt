package com.flagquest.game.models

interface AuthHandler {
    fun signIn(email: String, password: String, callback: (Boolean, String?, String?) -> Unit)
    fun signUp(email: String, password: String, callback: (Boolean, String?, String?) -> Unit)
    fun signOut()

    companion object {
        @Volatile private var instance: AuthHandler? = null

        fun getInstance(): AuthHandler {
            return instance ?: throw IllegalStateException("AuthHandler not initialized")
        }

        fun initialize(handler: AuthHandler) {
            if (instance == null) {
                instance = handler
            } else {
                throw IllegalStateException("AuthHandler already initialized")
            }
        }
    }
}
