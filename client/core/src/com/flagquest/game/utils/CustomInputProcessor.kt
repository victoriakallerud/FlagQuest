package com.flagquest.game.utils

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import com.flagquest.game.states.GameStateManager

class MyInputProcessor(private val gsm: GameStateManager): InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.BACK) {
            // Handle back button press here
            // Pop the current state or perform any other desired action
            gsm.pop()
            return true // Returning true indicates that the input event has been handled
        }
        return false // Returning false indicates that the event has not been handled
    }

    override fun keyUp(keycode: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun keyTyped(character: Char): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        TODO("Not yet implemented")
    }
}
