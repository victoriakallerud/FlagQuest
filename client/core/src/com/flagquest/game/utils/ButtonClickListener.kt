package com.flagquest.game.utils

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.MainMenuState
import com.flagquest.game.states.State
import org.json.JSONObject

/**
 * Listener that either takes a lazy state or an instruction(String) as input.
 * Upon clicking button, will refer user to said state or action.
 */
class ButtonClickListener(private val gsm: GameStateManager, private val lazyState: Lazy<State>?) : ClickListener() {
    private var instruction: String? = null
    constructor(gsm: GameStateManager, stateName: String) : this(gsm, null) {
        this.instruction = stateName
    }


    override fun clicked(event: InputEvent, x: Float, y: Float) {
        if (lazyState is Lazy<State>){
            val state = lazyState.value
            gsm.push(state)
        }
        // Custom Instructions
        if (instruction == "back")
            gsm.pop()
        if (instruction == "menu"){ //Go to main menu
            gsm.clear()
            gsm.push(MainMenuState(gsm))
        }
        if (instruction == "backToGame"){
            gsm.pop()
            SocketHandler.emit("resumeGame", JSONObject().put("lobbyId", DataManager.getData("lobbyId")).put("userId", DataManager.getData("userId")))
        }
        else
            println("Provide Proper Instruction!")
    }
}
