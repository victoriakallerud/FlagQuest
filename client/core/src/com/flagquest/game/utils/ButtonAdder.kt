package com.flagquest.game.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.State

object ButtonAdder {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")

    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height

    private val buttonHeight = screenHeight / 11
    private var buttonPos: Float = ((screenHeight / 2) + 150).toFloat()

    private val backButtonHeight = buttonHeight
    private val backButtonWidth = backButtonHeight


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

    fun addTextButtons (stage: Stage, gsm: GameStateManager, buttons: Array<Pair<TextButton, Lazy<State>>>, yTop: Float) {
        buttonPos = yTop
        for (button in buttons) {
            button.first.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.first.setPosition(screenWidth / 2 - button.first.width / 2, buttonPos)
            button.first.addListener(ButtonClickListener(gsm,button.second))
            stage.addActor(button.first)
            buttonPos -= button.first.height + 30
            println(button.first.text)
        }
    }

     fun addTitle(stage: Stage, title: String = "FLAGQUEST", fontScale: Float = 3.5f){
        val heading = Label(title, skin)
        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(fontScale)
        heading.setAlignment(Align.center)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        titleFont.data.setScale(1.5f)
        stage.addActor(heading)
    }
}