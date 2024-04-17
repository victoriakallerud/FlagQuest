package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.views.LobbyInitiationView
import com.flagquest.game.utils.ButtonClickListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class LobbyInitiationState(gsm: GameStateManager) : State(gsm), LobbyRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val view = LobbyInitiationView(gsm, stage, this)

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun redirectToLobbyState(lobbyId: String) {
        gsm.push(GameLobbyState(gsm, isAdmin = true, lobbyId))
    }

    override fun handleInput() {
        // TODO: Implement handleInput
    }
    override fun update(dt: Float) {
        view.update(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}