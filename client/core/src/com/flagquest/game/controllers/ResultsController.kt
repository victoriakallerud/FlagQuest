package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel

class ResultsController(private val userModel: UserApiModel, private val lobbyModel: LobbyApiModel) {
    fun addResults(): List<Pair<String, Int>> {
        var resultList: MutableList<Pair<String, Int>> = userModel.getResults()
        resultList.sortByDescending { it.second }
        return resultList
    }

    fun leaveLobby() {
        lobbyModel.leaveLobby()
        Gdx.app.log("ResultsController", "Leaving lobby")
    }

}