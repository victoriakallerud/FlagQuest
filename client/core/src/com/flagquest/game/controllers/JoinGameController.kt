package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.GameApiModel
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.utils.DataManager

class JoinGameController(private val lobbyModel: LobbyApiModel, private val gameModel: GameApiModel) {
    var redirectionListener: LobbyRedirectionListener? = null
    fun onCodeButtonClicked(code: String): Boolean {
        val lobby: String? = lobbyModel.putLobbyWithInviteCode(code)

        return if(lobby != null) {
            val lobbyId: String? = lobbyModel.getIdFromResponse(lobby)
            DataManager.setData("lobbyId", lobbyId!!)
            gameModel.joinGameOnSocket()
            redirectionListener?.redirectToLobbyState(lobbyId)
            true
        } else {
            Gdx.app.error("JoinGameController", "Failed to join lobby with code")
            false
        }
    }

    fun onRandomButtonClicked(): Boolean {
        val allLobbies: String? = lobbyModel.getAllLobbies()
        val firstLobby: String? = lobbyModel.getFirstLobby(allLobbies!!)
        val lobbyId: String? = lobbyModel.getIdFromResponse(firstLobby!!)
        val joinedLobbyId: String? = lobbyModel.putLobbyWithId(lobbyId!!)
        return if (joinedLobbyId != null) {
            DataManager.setData("lobbyId", joinedLobbyId)
            gameModel.joinGameOnSocket()
            redirectionListener?.redirectToLobbyState(lobbyId)
            true
        } else {
            Gdx.app.error("JoinGameController", "Failed to join random lobby")
            false
        }
    }
}