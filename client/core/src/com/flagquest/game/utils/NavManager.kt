package com.flagquest.game.utils

import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.LoginMenuState
import com.flagquest.game.states.MainMenuState
import com.flagquest.game.states.PauseState
import org.json.JSONObject

object NavManager {

    fun backButtonFunc (gsm: GameStateManager, backNavType: String) {
        when(backNavType) {
            "menu" -> {
                gsm.clear()
                gsm.push(MainMenuState(gsm))
            }
            "login" -> {
                gsm.clear()
                gsm.push(LoginMenuState(gsm))
            }
            "pause" -> {
                gsm.push(PauseState(gsm))
                SocketHandler.emit("pauseGame", JSONObject().put("lobbyId", DataManager.getData("lobbyId")).put("userId", DataManager.getData("userId")))
            }
            "back" -> {
                if (!gsm.isEmpty()) {
                    gsm.pop()
                }
            }
            "nothing" -> {}
            else -> throw Exception("Wrong backNavType!")
        }
    }
}