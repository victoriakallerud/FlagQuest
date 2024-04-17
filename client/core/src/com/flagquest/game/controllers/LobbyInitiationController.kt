package com.flagquest.game.controllers

import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener

class LobbyInitiationController(private val model: LobbyApiModel) {
    var redirectionListener: LobbyRedirectionListener? = null
    fun onCreateGameClicked(size: Int) {
        val lobby: String? = model.postLobby(size)
        println("Lobby: $lobby")
        val lobbyId: String = model.getIdFromResponse(lobby!!)
        redirectionListener?.redirectToLobbyState(lobbyId)
    }

}