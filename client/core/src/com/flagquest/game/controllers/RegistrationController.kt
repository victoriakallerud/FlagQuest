package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.MainMenuRedirectionListener

class RegistrationController(private val model: UserApiModel) {
    var redirectionListener: MainMenuRedirectionListener? = null
    var regErrorListener: ((String) -> Unit)? = null
    fun onRegisterClicked(authHandler: AuthHandler, email: String, password: String, userName: String) {
        model.registerUser(authHandler, email, password, userName) { registered ->
            if (registered) {
                Gdx.app.log("RegistrationState", "Registration successful")
                Gdx.app.postRunnable {
                    redirectionListener?.redirectToMainMenuState()
                }
            } else {
                Gdx.app.postRunnable {
                    Gdx.app.log("RegistrationController", "Registration failed")
                    regErrorListener?.invoke("Failed. Try again.")
                }
            }
        }
    }
}