package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.views.LobbyInitiationView

class LobbyInitiationState(gsm: GameStateManager) : State(gsm) {
    override val stage = Stage(ScreenViewport())
    private val view = LobbyInitiationView(gsm, stage)

    // TODO: Implement createBtn -> lobbyState transition

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun handleInput() {
        // TODO: Implement handleInput
    }
    override fun update(dt: Float) {
        view.update(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}