package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.MainMenuRedirectionListener
import com.flagquest.game.navigation.RegistrationRedirectionListener
import com.flagquest.game.utils.DataManager

class RegistrationController(private val model: UserApiModel) {
    var redirectionListener: MainMenuRedirectionListener? = null
    fun onRegisterClicked(authHandler: AuthHandler, email: String, password: String, userName: String, nationality: String): Boolean {
        var res = false
        model.registerUser(authHandler, email, password, userName, nationality) { registered ->
            if (registered) {
                Gdx.app.log("RegistrationState", "Registration successful")
                Gdx.app.postRunnable {
                    redirectionListener?.redirectToMainMenuState()
                }
                res = true
            } else {
                res = false
            }
        }
       return res;
    }
}