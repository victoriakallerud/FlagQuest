package com.flagquest.game.utils

import com.badlogic.gdx.scenes.scene2d.Stage
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.MainMenuState
import com.flagquest.game.states.PauseState

object NavManager {

    fun backButtonFunc (stage: Stage, gsm: GameStateManager, backNavType: String) {
        when(backNavType) {
            "menu" -> {
                gsm.clear()
                gsm.push(MainMenuState(gsm))
            }
            "pause" -> gsm.push(PauseState(gsm))
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