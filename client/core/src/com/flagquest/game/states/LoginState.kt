package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.navigation.MainMenuRedirectionListener
import com.flagquest.game.views.LoginView

class LoginState(gsm: GameStateManager, authHandler: AuthHandler) : State(gsm), MainMenuRedirectionListener {
    override val stage = Stage(ScreenViewport())

    init {
        Gdx.input.inputProcessor = stage
        LoginView(gsm, stage, this, authHandler)
    }

    override fun redirectToMainMenuState() {
        gsm.push(MainMenuState(gsm))
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
