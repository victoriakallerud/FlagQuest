package com.flagquest.game.controllers

import com.flagquest.game.models.GameApiModel
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.utils.DataManager

class LobbyInitiationController(private val lobbyModel: LobbyApiModel, private val gameModel: GameApiModel) {
    var redirectionListener: LobbyRedirectionListener? = null
    fun onCreateGameClicked(size: Int, level: String): Boolean {
        val lobby: String? = lobbyModel.postLobby(size, level)
        return if(lobby != null) {
            val lobbyId: String? = lobbyModel.getIdFromResponse(lobby)
            DataManager.setData("lobbyId", lobbyId!!)
            gameModel.joinGameOnSocket()
            redirectionListener?.redirectToLobbyState(lobbyId)
            true
        } else {
            false
        }

    }

}
