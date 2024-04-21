package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.OfflineGameController
import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.navigation.TrainingQuestionRedirectionListener
import com.flagquest.game.views.OfflineGameView

class OfflineGameState(gsm: GameStateManager, private val fromLogin: Boolean) : State(gsm),
    TrainingQuestionRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val controller: OfflineGameController = OfflineGameController(LocalApiModel())
    override var backNavType = "nothing"

    init {
        Gdx.input.inputProcessor = stage
        backNavType = if (fromLogin) {
            "login"
        } else {
            "menu"
        }
        OfflineGameView(gsm, stage, this, controller, fromLogin)
    }
    override fun update(dt: Float) {
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }

    override fun redirectToNewTrainingQuestion() {
        gsm.push(OfflineGameState(gsm, fromLogin))

    }
}
