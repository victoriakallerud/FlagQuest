package com.flagquest.game.utils

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.State
class ButtonClickListener(private val gsm: GameStateManager, private val lazyState: Lazy<State>?) : ClickListener() {
    override fun clicked(event: InputEvent, x: Float, y: Float) {
        if (lazyState is Lazy<State>){
            val state = lazyState.value
            gsm.push(state)
        }
        else
            println("LINK TO STATE!")
    }
}
