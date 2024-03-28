package com.flagquest.game.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class State(protected val gsm: GameStateManager) {
    protected var cam: OrthographicCamera = OrthographicCamera()
    protected var mouse: Vector3 = Vector3()

    abstract fun handleInput()
    abstract fun update(dt: Float) // dt = delta time (difference between two frames)
    abstract fun render()
}