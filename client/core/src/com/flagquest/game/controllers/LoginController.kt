package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.MainMenuRedirectionListener


class LoginController(private val model: UserApiModel) {
    var redirectionListener: MainMenuRedirectionListener? = null
    var loginErrorListener: ((String) -> Unit)? = null

    fun onLoginClicked(authHandler: AuthHandler, username: String, password: String){
        model.loginUser(authHandler, username, password) { loggedIn ->
            Gdx.app.log("LoginController", "Login result: $loggedIn")
            if (loggedIn) {
                Gdx.app.log("LoginController", "Login successful")
                Gdx.app.postRunnable {
                    redirectionListener?.redirectToMainMenuState()
                }
            } else {
                Gdx.app.postRunnable {
                    Gdx.app.log("LoginController", "Login failed")
                    loginErrorListener?.invoke("Login failed. Try again.")
                }
            }
        }
    }

}