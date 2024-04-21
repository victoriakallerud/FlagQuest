package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.HighscoreController
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.views.HighscoreView

class HighscoreState(gsm: GameStateManager) : State(gsm) {
    override val stage = Stage(ScreenViewport())
    private val controller = HighscoreController(UserApiModel())

    init {
        Gdx.input.inputProcessor = stage
        HighscoreView(gsm, stage, controller)
    }
    override fun update(dt: Float) {
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}