package com.flagquest.game.utils

import com.badlogic.gdx.scenes.scene2d.Stage
import com.flagquest.game.states.GameStateManager

object NavManager {

    fun backButtonFunc (stage: Stage, gsm: GameStateManager, backNavType: String) {
        when(backNavType) {
            "menu" -> println("") //TODO: Implement menu
            "pause" -> println("") //TODO: Implement pause
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