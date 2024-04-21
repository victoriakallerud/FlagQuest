package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.views.MainMenuView

class MainMenuState(gsm: GameStateManager) : State(gsm) {
    override val stage = Stage(ScreenViewport())
    private val view = MainMenuView(stage)

    init {
        Gdx.input.inputProcessor = stage
        setUpButtonListeners()
    }

    // Set up button listeners - in State, because it needs access to gsm and handles state transitions
    private fun setUpButtonListeners() {
        view.buttons[0].addListener(ButtonClickListener(gsm, lazy { LobbyInitiationState(gsm) }))
        view.buttons[1].addListener(ButtonClickListener(gsm, lazy { JoinGameState(gsm) }))
        view.buttons[2].addListener(ButtonClickListener(gsm, lazy { OfflineGameState(gsm, false) }))
        view.buttons[3].addListener(ButtonClickListener(gsm, lazy { HighscoreState(gsm) }))
        view.buttons[4].addListener(ButtonClickListener(gsm, lazy { ManageFriendsState(gsm) }))
    }


    override fun update(dt: Float) {
        view.update(dt)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        view.render()
    }
}

