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
import com.flagquest.game.utils.NavManager.backButtonFunc

object UIManager {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")

    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height

    // Button parameters
    private val buttonHeight = screenHeight / 11
    private var buttonPos: Float = ((screenHeight / 2) + 150).toFloat()

    // Backbutton parameters
    private val backButtonHeight = buttonHeight
    private val backButtonWidth = backButtonHeight


    fun addBackButton(stage: Stage, gsm: GameStateManager, backNavType: String) {
        val backButton = TextButton("<-", skin) // Create the back button
        backButton.width = backButtonWidth.toFloat()
        backButton.height = backButtonHeight.toFloat()
        backButton.setPosition((screenWidth / 11).toFloat(), (screenWidth / 11).toFloat())
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                backButtonFunc(stage,gsm, backNavType) // Pop the current state
            }
        })
        stage.addActor(backButton) // Add the back button to the stage
    }
    //No function for mixed array, lmk if you know a solution.
    fun addNavButtonArray (stage: Stage, gsm: GameStateManager, buttons: Array<Pair<TextButton, Lazy<State>>>, yTop: Float) {
        buttonPos = yTop
        for (button in buttons) {
            addNavButton(stage,gsm,button, buttonPos)
            buttonPos -= button.first.height + 30 // Adjust inter-button distance here
        }
    }
    fun addInstructButtonArray (stage: Stage, gsm: GameStateManager, buttons: Array<Pair<TextButton, String>>, yTop: Float) {
        buttonPos = yTop
        for (button in buttons) {
            addInstructButton(stage,gsm,button, buttonPos)
            buttonPos -= button.first.height + 30 // Adjust inter-button distance here
        }
    }

    fun addNavButton (stage: Stage, gsm: GameStateManager, button: Pair<TextButton,Lazy<State>>, yTop: Float){
        button.first.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        button.first.setPosition(screenWidth / 2 - button.first.width / 2, yTop)
        button.first.addListener(ButtonClickListener(gsm,button.second))
        stage.addActor(button.first)
    }
    fun addInstructButton (stage: Stage, gsm: GameStateManager, button: Pair<TextButton, String>, yTop: Float){
        button.first.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        button.first.setPosition(screenWidth / 2 - button.first.width / 2, yTop)
        button.first.addListener(ButtonClickListener(gsm,button.second))
        stage.addActor(button.first)
    }

     fun addHeading(stage: Stage, title: String, fontScale: Float = 2.8f){
        val heading = Label(title, skin)
        heading.style = Label.LabelStyle(titleFont, heading.style.fontColor)
        heading.setFontScale(fontScale)
        heading.setAlignment(Align.center)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)
    }
}