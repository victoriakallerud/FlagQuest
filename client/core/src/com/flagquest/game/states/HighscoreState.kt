package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.HighscoreController
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.utils.UIManager
import okhttp3.OkHttpClient
import okhttp3.Request
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.views.HighscoreView
import com.flagquest.game.views.LobbyInitiationView

class HighscoreState(gsm: GameStateManager) : State(gsm) {
    override val stage = Stage(ScreenViewport())
    private val controller = HighscoreController(UserApiModel())
    private val view = HighscoreView(gsm, stage, controller)

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
}