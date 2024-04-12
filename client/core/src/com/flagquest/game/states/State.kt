package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage

abstract class State(protected val gsm: GameStateManager) {
    protected var cam: OrthographicCamera = OrthographicCamera()
    protected var mouse: Vector3 = Vector3()
    protected abstract val stage: Stage

    abstract fun handleInput()
    abstract fun update(dt: Float) // dt = delta time (difference between two frames)
    abstract fun render()
    fun switchInputProcessor(){
        Gdx.input.inputProcessor = stage
    }
}