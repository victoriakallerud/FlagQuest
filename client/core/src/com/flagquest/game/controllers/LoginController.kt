package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.MainMenuRedirectionListener


class LoginController(private val model: UserApiModel) {
    var redirectionListener: MainMenuRedirectionListener? = null

    fun onLoginClicked(authHandler: AuthHandler, username: String, password: String): Boolean {
        var res = false
        model.loginUser(authHandler, username, password) { loggedIn ->
            Gdx.app.log("LoginState", "Login result: $loggedIn")
            if (loggedIn) {
                Gdx.app.log("LoginState", "Login successful")
                Gdx.app.postRunnable {
                    redirectionListener?.redirectToMainMenuState()
                }
                res = true
            } else {
                res = false
            }
        }
        return res
    }
}