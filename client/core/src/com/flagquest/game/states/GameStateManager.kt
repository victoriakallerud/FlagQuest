package com.flagquest.game.states

import java.util.Stack
class GameStateManager {
    var states: Stack<State> = Stack()

    fun push(state: State) {
        states.push(state)
        state.switchInputProcessor()
    }
    fun pop() {
        states.pop()
        states.peek()?.switchInputProcessor()
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
    fun isEmpty(): Boolean {
        return states.isEmpty()
    }
}