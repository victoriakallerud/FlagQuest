package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.navigation.ResultRedirectionListener
import com.flagquest.game.navigation.TrainingQuestionRedirectionListener
import com.flagquest.game.views.OnlineGameView

// Use without arguments when first presenting the quiz. Load with chosen and correct answer to reveal.
class OnlineGameState(gsm: GameStateManager, chosen: String? = null, correct: String? = null) : State(gsm),
    OnlineGameRedirectionListener, ResultRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val view = OnlineGameView(gsm, stage, this, this)


    init {
        Gdx.input.inputProcessor = stage
        addListenersToViewButtons()
    }

    private fun addListenersToViewButtons() {

    }

    fun refreshState() {
        gsm.set(OfflineGameState(gsm, false))
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

    override fun redirectToOnlineGameState() {
        gsm.push(OnlineGameState(gsm))
    }

    override fun redirectToResultState() {
        Gdx.app.log("OnlineGameState", "Redirecting to result state")
        gsm.push(ResultsState(gsm))
    }
}
