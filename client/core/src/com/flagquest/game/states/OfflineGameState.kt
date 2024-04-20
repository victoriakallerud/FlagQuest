package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.OfflineGameController
import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.navigation.TrainingQuestionRedirectionListener
import com.flagquest.game.views.OfflineGameView

// Use without arguments when first presenting the quiz. Load with chosen and correct answer to reveal.
class OfflineGameState(gsm: GameStateManager, fromLogin: Boolean, chosen: String? = null, correct: String? = null) : State(gsm),
    TrainingQuestionRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val controller: OfflineGameController = OfflineGameController(LocalApiModel())
    private val login: Boolean = fromLogin
    private val view = OfflineGameView(gsm, stage, this, controller, login)


    init {
        Gdx.input.inputProcessor = stage
        addListenersToViewButtons()
    }

    private fun addListenersToViewButtons() {

    }

    fun refreshState() {
        gsm.set(OfflineGameState(gsm, login))
    }

    override fun handleInput() {
        // TODO: Implement handleInput
    }
    override fun update(dt: Float) {
        handleInput()
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }

    override fun redirectToNewTrainingQuestion() {
        gsm.push(OfflineGameState(gsm, login))

    }
}
