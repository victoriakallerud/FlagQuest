package com.flagquest.game.controllers

import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.RegistrationRedirectionListener
import com.flagquest.game.utils.DataManager

class RegistrationController(private val model: UserApiModel) {
    var redirectionListener: RegistrationRedirectionListener? = null
    fun onRegisterClicked(name: String, username: String, nationality: String, password: String) {
        val user: String? = model.postUser(name, username, nationality, password)
        val userId: String = model.getIdFromResponse(user!!)
        DataManager.setData("userId", userId)
        redirectionListener?.redirectToMainMenuState()
    }
}