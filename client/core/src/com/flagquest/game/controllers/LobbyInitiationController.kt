package com.flagquest.game.controllers

import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.utils.DataManager

class LobbyInitiationController(private val model: LobbyApiModel) {
    var redirectionListener: LobbyRedirectionListener? = null
    fun onCreateGameClicked(size: Int, level: String): Boolean {
        val lobby: String? = model.postLobby(size, level)
        return if(lobby != null) {
            val lobbyId: String = model.getIdFromResponse(lobby)
            DataManager.setData("lobbyId", lobbyId)
            redirectionListener?.redirectToLobbyState(lobbyId)
            true
        } else {
            false
        }

    }

}
