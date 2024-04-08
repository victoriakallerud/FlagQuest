package com.flagquest.game.utils

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.LobbyInitiationState

class ButtonClickListener(private val gsm: GameStateManager, private val buttonText: String) : ClickListener() {

    override fun clicked(event: InputEvent, x: Float, y: Float) {
        when (buttonText) {
            "CREATE GAME" -> gsm.push(LobbyInitiationState(gsm))
            "JOIN GAME" -> println("Handle JOIN GAME button click here")
            "TRAINING MODE" -> println("Handle TRAINING MODE button click here")
            "HIGHSCORE BOARD" -> println("Handle HIGHSCORE BOARD button click here")
            "MANAGE FRIENDS" -> println("Handle MANAGE FRIENDS button click here")
            else -> println("Unknown button clicked")
        }
    }
}
