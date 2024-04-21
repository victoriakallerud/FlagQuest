package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.views.ManageFriendsView

class ManageFriendsState(gsm: GameStateManager) : State(gsm) {
    override val stage = Stage(ScreenViewport())

    init {
        ManageFriendsView(gsm, stage)
        Gdx.input.inputProcessor = stage
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