package com.flagquest.game.controllers

import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.views.LobbyInitiationView

class LobbyInitiationController(private val model: LobbyApiModel, private val view: LobbyInitiationView) {
    fun onCreateGameClicked(size: Int): String? {
        return model.createLobby(size)
    }

}