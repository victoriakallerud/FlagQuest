package com.flagquest.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.flagquest.game.states.GameLobbyState
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.JoinGameState
import com.flagquest.game.states.LobbyInitiationState
import com.flagquest.game.states.LoginMenuState
import com.flagquest.game.states.LoginState
import com.flagquest.game.states.MainMenuState
import com.flagquest.game.states.ManageFriendsState
import com.flagquest.game.states.RegistrationState

class FlagQuest : ApplicationAdapter() {
    var gsm: GameStateManager = GameStateManager()
    var batch: SpriteBatch? = null
    var img: Texture? = null
    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        gsm.push(MainMenuState(gsm))
        gsm.push(LoginMenuState(gsm))
        gsm.push(LoginState(gsm))
        gsm.push(RegistrationState(gsm))
        gsm.push(LobbyInitiationState(gsm))
        gsm.push(GameLobbyState(gsm, true))
        gsm.push(JoinGameState(gsm))
        gsm.push(ManageFriendsState(gsm))
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        gsm.update(Gdx.graphics.deltaTime)
        gsm.render()
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }
}
