package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.navigation.PauseRedirectionListener
import com.flagquest.game.navigation.ResultRedirectionListener
import com.flagquest.game.views.OnlineGameView

class OnlineGameState(gsm: GameStateManager) : State(gsm),
    OnlineGameRedirectionListener, ResultRedirectionListener, PauseRedirectionListener {
    override val stage = Stage(ScreenViewport())
    override var backNavType = "nothing"

    init {
        Gdx.input.inputProcessor = stage
        OnlineGameView(gsm, stage, this, this, this)
    }

    override fun update(dt: Float) {
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

    override fun redirectToPauseGameState(){
        gsm.push(PauseState(gsm))
    }


}
