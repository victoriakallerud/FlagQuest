package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.LobbyInitiationController
import com.flagquest.game.models.GameApiModel
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager

class LobbyInitiationView(gsm: GameStateManager, private val stage: Stage, listener: LobbyRedirectionListener, private val controller: LobbyInitiationController) {
    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont
    private var screenWidth = UIManager.screenWidth
    private var pos: Float = ((UIManager.screenHeight / 2) + 50).toFloat()
    private val sizeInput = TextField("", skin).apply{ messageText="How many players?"}
    private val createBtn = TextButton("CREATE LOBBY", skin)
    private var size: Int = 6
    val backNavType = "menu"
    private val europe: TextButton = TextButton("Europe", skin)
    private val asia: TextButton = TextButton("Asia", skin)
    private val africa: TextButton = TextButton("Africa", skin)
    private val northamerica: TextButton = TextButton("North America", skin)
    private val southamerica: TextButton = TextButton("South America", skin)
    private val oceania: TextButton = TextButton("Oceania", skin)
    private val all: TextButton = TextButton("All", skin)
    private val levels = arrayOf(europe, asia, africa, northamerica, southamerica, oceania, all)
    private var currentLevel = all

    init {
        controller.redirectionListener = listener

        textFieldStyle.font.data.setScale(1.2f)

        UIManager.addHeading(stage, "CREATE GAME\nLOBBY", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

        createBtn.setColor(0.349f, 0.631f, 0.541f, 1f)

        var table = Table()
        table.width = screenWidth.toFloat()
        table.setPosition((stage.width - table.width) / 2, 10f)
        stage.addActor(table)

        updateLevels(table)

        for (level in levels) {
            level.addListener(
                object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        currentLevel.color = level.color
                        currentLevel = level
                        updateLevels(table)
                    }
                }
            )
        }

        sizeInput.textFieldFilter = TextField.TextFieldFilter { textField, c ->
            Character.isDigit(c)
        }
        
        // Add padding to TextField
        skin.get(TextField.TextFieldStyle::class.java).apply {
            background.leftWidth = 50f // Set left padding
        }

        sizeInput.width = (screenWidth*80/100).toFloat()
        sizeInput.height = UIManager.elementHeight.toFloat()
        sizeInput.setPosition(screenWidth / 2 - sizeInput.width / 2, pos - UIManager.elementHeight - UIManager.elementSpacing)
        stage.addActor(sizeInput)

        var counter = 2
        createBtn.width = (screenWidth*80/100).toFloat()
        createBtn.height = UIManager.elementHeight.toFloat()
        createBtn.setPosition(screenWidth / 2 - createBtn.width / 2, pos - (UIManager.elementHeight + UIManager.elementSpacing) * counter)
        stage.addActor(createBtn)

        createBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (sizeInput.text != "") {
                    size = sizeInput.text.toInt()
                }
                if(!controller.onCreateGameClicked(size, currentLevel.text.toString())) {
                    Gdx.app.log("LobbyInitiationView", "Failed to create lobby")
                    showError("Failed to create lobby")
                }
            }
        })
        titleFont.data.setScale(1.5f)
    }

    fun updateLevels(table: Table) {
        var index = 0
        for (row in 0 until 4) {  // Four rows
            for (col in 0 until 2) {  // Two columns
                if (index < levels.size) {
                    if (levels[index] == currentLevel) {
                        levels[index].setColor(0.6f, 0.79607844f, 0.89411765f, 1f)
                    }
                    table.add(levels[index]).width(480f).height(120f).pad(5f)
                    index++
                } else {
                    // Add empty cells if there are no more elements to add
                    table.add().width(480f).height(120f).pad(5f)
                }
            }
            table.row()  // Move to the next row
        }
        table.pad(10f)
        table.defaults().pad(5f)
        table.pack() // This is important to size the table according to its contents
        //table.setPosition((stage.width - table.prefWidth) / 2, 180f)
        table.setPosition((stage.width - table.prefWidth) / 2, UIManager.screenHeight - 600f - table.height)
    }

    fun showError(error: String) {
        UIManager.addError(stage, error)
    }

    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}