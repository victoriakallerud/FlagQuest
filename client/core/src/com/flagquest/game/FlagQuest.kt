package com.flagquest.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.ScreenUtils
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.LoginMenuState

class FlagQuest : ApplicationAdapter() {
    var gsm: GameStateManager = GameStateManager()
    override fun create() {
        Gdx.input.isCatchBackKey = true
        gsm.push(LoginMenuState(gsm))
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        gsm.update(Gdx.graphics.deltaTime)
        gsm.render()
    }

    override fun dispose() {  }
}
