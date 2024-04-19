package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.DataManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.SocketHandler
import com.flagquest.game.views.GameLobbyView
import com.flagquest.game.views.LobbyInitiationView
import io.socket.client.Socket
import org.json.JSONArray

class GameLobbyState(gsm: GameStateManager, isAdmin: Boolean, lobbyId: String) : State(gsm), OnlineGameRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val view = GameLobbyView(gsm, isAdmin, lobbyId, stage, this)

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun redirectToOnlineGameState() {
        Gdx.app.log("GameLobbyState", "redirectToOnlineGameState")
        gsm.push(OnlineGameState(gsm))
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