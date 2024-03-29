package com.flagquest.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.LoginMenuState
import com.flagquest.game.states.LoginState
import com.flagquest.game.states.MainMenuState

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
