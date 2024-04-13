package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.flagquest.game.utils.NavManager.backButtonFunc

abstract class State(protected val gsm: GameStateManager) {
    protected var cam: OrthographicCamera = OrthographicCamera()
    protected var mouse: Vector3 = Vector3()
    protected abstract val stage: Stage
    private var backButtonListenerAdded: Boolean = false
    protected open var backNavType : String = "back"
    abstract fun handleInput()
    abstract fun update(dt: Float) // dt = delta time (difference between two frames)
    abstract fun render()
    fun switchInputProcessor(){
        if (!backButtonListenerAdded){ //Adds the system back press listener to the stage if not yet added.
            when(backNavType) {
                "menu" -> println("") //TODO: Implement menu
                "pause" -> println("") //TODO: Implement pause
                else -> {
                    stage.addListener(backButtonListener)
                    backButtonListenerAdded = true
                }
            }
        }
        Gdx.input.inputProcessor = stage //Switches the input processor to that of the state to be shown.
    }

    // Handles system back button presses s.t. state is popped.
    private val backButtonListener = object : InputListener() {
        override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
            // Check if the back button is pressed
            if (keycode == Input.Keys.BACK) {
                // Handle back button press here
                backButtonFunc(stage,gsm,backNavType)
                return true
            }
            return super.keyDown(event, keycode)
        }
    }
}