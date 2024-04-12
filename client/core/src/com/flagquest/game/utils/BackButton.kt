package com.flagquest.game.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.states.GameStateManager

object BackButtonUtils {
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height

    private val backButtonHeight = screenHeight / 11
    private val backButtonWidth = backButtonHeight

    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))

    fun addBackButton(stage: Stage, gsm: GameStateManager) {
        val backButton = TextButton("<-", skin) // Create the back button
        backButton.width = backButtonWidth.toFloat()
        backButton.height = backButtonHeight.toFloat()
        backButton.setPosition((screenWidth / 11).toFloat(), (screenWidth / 11).toFloat())
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gsm.pop() // Pop the current state
            }
        })
        stage.addActor(backButton) // Add the back button to the stage
    }
}