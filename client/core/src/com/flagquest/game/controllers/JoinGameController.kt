package com.flagquest.game.controllers

import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.GameRedirectionListener

class JoinGameController(private val model: LobbyApiModel) {
    var redirectionListener: GameRedirectionListener? = null
    fun onCodeButtonClicked(code: String) {
        val lobby: String? = model.putLobbyWithInviteCode(code)
        val lobbyId: String = model.getIdFromResponse(lobby!!)
        redirectionListener?.redirectToGameState(lobbyId)
    }

    fun onRandomButtonClicked() {
        println("onRandomButtonClicked() function run")
        val allLobbies: String? = model.getAllLobbies()
        val firstLobby: String? = model.getFirstLobby(allLobbies!!)
        val lobbyId: String = model.getIdFromResponse(firstLobby!!)
        redirectionListener?.redirectToGameState(lobbyId)
        println("Controller -> State should be done")
    }
}