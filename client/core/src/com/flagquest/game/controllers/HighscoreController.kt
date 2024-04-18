package com.flagquest.game.controllers

import com.flagquest.game.models.UserApiModel

class HighscoreController(private val model: UserApiModel) {
    fun addGlobalHighscores(): List<Pair<String, Int>> {
        val highscores: String? = model.getTopTenUsers()
        return model.parseHighscores(highscores)
    }

    fun addFriendsHighscores(): MutableList<Pair<String, Int>> {
        var friendHighscoreList: MutableList<Pair<String, Int>> = model.getFriendHighscores()
        friendHighscoreList.sortByDescending { it.second }
        friendHighscoreList = friendHighscoreList.take(10).toMutableList()
        return friendHighscoreList
    }

}