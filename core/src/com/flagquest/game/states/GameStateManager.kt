package com.flagquest.game.states

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.Stack
class GameStateManager {
    var states: Stack<State> = Stack()

    fun push(state: State) {
        states.push(state)
    }
    fun pop() {
        states.pop()
    }
    fun set(state: State) {
        states.pop()
        states.push(state)
    }
    fun update(dt: Float) {
        states.peek().update(dt)
    }
    fun render() {
        states.peek().render()
    }
}