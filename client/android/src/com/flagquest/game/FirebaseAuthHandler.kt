package com.flagquest.game

import com.flagquest.game.models.AuthHandler
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthHandler : AuthHandler {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun signIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            callback(task.isSuccessful, task.exception?.message)
        }
    }

    override fun signUp(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            callback(task.isSuccessful, task.exception?.message)
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}