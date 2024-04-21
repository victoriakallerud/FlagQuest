package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.GameLobbyController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.DataManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.SocketHandler
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.views.GameLobbyView

class GameLobbyState(gsm: GameStateManager, isAdmin: Boolean, lobbyId: String) : State(gsm), OnlineGameRedirectionListener, LobbyRedirectionListener {
    override val stage = Stage(ScreenViewport())
    private val controller: GameLobbyController = GameLobbyController(UserApiModel(), LobbyApiModel())
    private val view = GameLobbyView(gsm, isAdmin, lobbyId, stage, controller, this, this)

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun redirectToOnlineGameState() {
        Gdx.app.log("GameLobbyState", "redirectToOnlineGameState")
        gsm.push(OnlineGameState(gsm))
    }

    override fun redirectToLobbyState(lobbyId: String) {
        Gdx.app.log("GameLobbyState", "redirectToLobbyState")
        gsm.push(GameLobbyState(gsm, true, lobbyId))
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