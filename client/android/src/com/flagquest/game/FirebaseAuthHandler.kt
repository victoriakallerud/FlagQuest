package com.flagquest.game

import com.flagquest.game.models.AuthHandler
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthHandler : AuthHandler {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun signIn(email: String, password: String, callback: (Boolean, String?, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // If the task is successful, extract the user ID
                val uid = task.result?.user?.uid
                callback(true, uid, null)
            } else {
                // If the task failed, extract the error message
                val errorMessage = task.exception?.message
                callback(false, null, errorMessage)
            }
        }
    }

    override fun signUp(email: String, password: String, callback: (Boolean, String?, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val uid = task.result?.user?.uid
                callback(true, uid, null)
            } else {
                val errorMessage = task.exception?.message
                callback(false, null, errorMessage)
            }
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}