package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.JoinGameController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.GameRedirectionListener
import com.flagquest.game.views.JoinGameView

class JoinGameState(gsm: GameStateManager) : State(gsm), GameRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val controller: JoinGameController = JoinGameController(LobbyApiModel())
    private val view = JoinGameView(gsm, stage, this, controller)

    init {
        Gdx.input.inputProcessor = stage
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

    override fun redirectToGameState(lobbyId: String) {
        println("GameLobby should be pushed 1")
        gsm.push(GameLobbyState(gsm, isAdmin = false, lobbyId))
        println("GameLobby should be pushed 2")
    }
}