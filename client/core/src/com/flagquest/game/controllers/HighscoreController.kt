package com.flagquest.game.controllers

import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener

class HighscoreController(private val model: UserApiModel) {
    // var redirectionListener: LobbyRedirectionListener? = null
    fun addGlobalHighscores(): List<Pair<String, Int>> {
        val highscores: String? = model.getTopTenUsers()
        val highscoreList: List<Pair<String, Int>> = model.parseHighscores(highscores)
        return highscoreList
    }

    fun addFriendsHighscores() {

    }

}