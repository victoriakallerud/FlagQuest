package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.navigation.MainMenuRedirectionListener
import com.flagquest.game.views.LoginView

class LoginState(gsm: GameStateManager, private val authHandler: AuthHandler) : State(gsm), MainMenuRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val view = LoginView(gsm, stage, this, authHandler)

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun redirectToMainMenuState() {
        gsm.push(MainMenuState(gsm))
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
}
