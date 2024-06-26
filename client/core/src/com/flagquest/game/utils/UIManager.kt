package com.flagquest.game.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.State
import com.flagquest.game.utils.NavManager.backButtonFunc

object UIManager {
    val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    val titleFont: BitmapFont = skin.getFont("title")

    val screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height

    // Element parameters
    val elementHeight = screenHeight / 11
    val elementWidth = screenWidth * 8 / 10
    val elementSpacing = 20
    private var elementPos: Float = ((screenHeight / 2) + 150).toFloat()
    val buttonTextScale = 1.5f

    // Backbutton parameters
    private val backButtonHeight = elementHeight
    private val backButtonWidth = backButtonHeight


    fun addBackButton(stage: Stage, gsm: GameStateManager, backNavType: String) {
        val backButton = TextButton("<-", skin) // Create the back button
        backButton.width = backButtonWidth.toFloat()
        backButton.height = backButtonHeight.toFloat()
        backButton.setPosition((screenWidth / 11).toFloat(), (screenWidth / 11).toFloat())
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                backButtonFunc(gsm, backNavType) // Pop the current state
            }
        })
        stage.addActor(backButton) // Add the back button to the stage
    }
    fun addInstructButtonArray (stage: Stage, gsm: GameStateManager, buttons: Array<Pair<TextButton, String>>, yTop: Float) {
        elementPos = yTop
        for (button in buttons) {
            addInstructButton(stage,gsm,button, elementPos)
            elementPos -= button.first.height + 30 // Adjust inter-button distance here
        }
    }

    /**
     * Adds a button that pushes the associated state.
     */
    fun addNavButton (stage: Stage, gsm: GameStateManager, button: Pair<TextButton,Lazy<State>>, yTop: Float){
        button.first.setSize((screenWidth*80/100).toFloat(), elementHeight.toFloat())
        button.first.setPosition(screenWidth / 2 - button.first.width / 2, yTop)
        button.first.addListener(ButtonClickListener(gsm,button.second))
        stage.addActor(button.first)
    }

    /**
     * This function adds a button which contains a string for instructions.
     * Carried out as documented in ButtonClickListener.kt
     */
    private fun addInstructButton (stage: Stage, gsm: GameStateManager, button: Pair<TextButton, String>, yTop: Float){
        button.first.setSize((screenWidth*80/100).toFloat(), elementHeight.toFloat())
        button.first.setPosition(screenWidth / 2 - button.first.width / 2, yTop)
        button.first.addListener(ButtonClickListener(gsm,button.second))
        stage.addActor(button.first)
    }
    fun addPauseButton(stage: Stage, gsm: GameStateManager, color: Color, size: Float){
        val pauseTexture = Texture(Gdx.files.internal("skins/raw/button-pause.png"))
        val pauseSprite = Sprite(pauseTexture)
        pauseSprite.setSize(size, size)

        // Set the tint color of the sprite
        pauseSprite.color = color

        val pauseDrawable = SpriteDrawable(pauseSprite)

        val pauseButton = ImageButton(pauseDrawable).apply {
            setPosition(((screenWidth - width) / 2), (screenWidth / 13).toFloat())
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    backButtonFunc(gsm, "pause") // Go to pause screen
                }
            })
        }
        stage.addActor(pauseButton)
    }

    fun addHeading(stage: Stage, title: String, fontScale: Float = 2.8f, posY : Float = screenHeight - 500f){
        val heading = Label(title, skin)
        heading.style = Label.LabelStyle(titleFont, heading.style.fontColor)
        heading.setFontScale(fontScale)
        heading.setAlignment(Align.center)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, posY)
        stage.addActor(heading)
    }

    fun addScrollPane(table: Table): ScrollPane {
        val scrollPane = ScrollPane(table, skin).apply {
            setScrollingDisabled(true, false)
            setFadeScrollBars(false)

            // Calculate the position to center the scroll pane
            val scrollPaneWidth = (screenWidth / 100 * 80).toFloat() // Assuming 80% of the screen width
            val scrollPaneHeight = (screenHeight / 100 * 48).toFloat() // Assuming a fixed height
            val xPos = (screenWidth - scrollPaneWidth) / 2
            val yPos = (screenHeight - scrollPaneHeight) / 2

            // Set the position of the scroll pane
            setPosition(xPos, yPos)
            setSize(scrollPaneWidth, scrollPaneHeight)
        }
        return scrollPane
    }
    fun truncateString(input: String, maxLength: Int): String {
        return if (input.length <= maxLength) {
            input // Return the original string if it's within the maxLength
        } else {
            // Otherwise, truncate the string and append an ellipsis
            input.substring(0, maxLength - 3) + "..."
        }
    }

    fun addError(stage: Stage, error: String){
        val errorStyle = skin.get("error", Label.LabelStyle::class.java)
        val errorLabel = Label(error, errorStyle)
        errorLabel.setFontScale(1.5f)
        errorLabel.setAlignment(Align.center)
        errorLabel.pack()
        errorLabel.setPosition((screenWidth - errorLabel.prefWidth) / 2, screenHeight - 600f)
        stage.addActor(errorLabel)
    }

    fun removeErrors(stage: Stage) {
        stage.actors.forEach {
            if (it is Label && it.style == skin.get("error", Label.LabelStyle::class.java)) {
                it.remove()
            }
        }
    }
}