package com.flagquest.game.states

import java.util.Stack
class GameStateManager {
    var states: Stack<State> = Stack()

    fun push(state: State) {
        states.push(state)
        state.switchInputProcessor() //Ensure that the inputs of the new state are taken
    }
    fun pop() {
        states.pop()
        states.peek()?.switchInputProcessor() //Ensure that inputs of previous state are taken up again.
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
    fun clear(){
        states.clear()
    }
}