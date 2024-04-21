package com.flagquest.game.views

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.flagquest.game.controllers.ResultsController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.MainMenuState
import com.flagquest.game.utils.UIManager
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.UIManager.addScrollPane
import com.flagquest.game.utils.UIManager.screenHeight

class ResultsView(gsm: GameStateManager, private val stage: Stage) {
    val controller: ResultsController = ResultsController(UserApiModel(), LobbyApiModel())

    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont

    private var results = mutableListOf<Pair<String, Int>>()
    private val scoreTable = Table()

    init {
        // Font size adjustments
        textFieldStyle.font.data.setScale(5f)
        titleFont.data.setScale(1.2f)

        addHeading(stage, "RESULTS", 2.8f)

        // Fetch results
        results = controller.addResults().toMutableList()

        // Order results into table
        var ranking = 1
        for (player in results) {
            val labels = listOf(Label(ranking.toString(), skin),Label(player.first, skin), Label(player.second.toString(), skin))
            for (label in labels) {
                // Styling
                label.style = Label.LabelStyle(titleFont, skin.getColor("textColor"))
                label.setFontScale(1.8f)
                scoreTable.add(label).align(Align.left).pad(10f).padLeft(35f)
            }
            scoreTable.row()
            ranking++
        }

        // Position in ScrollPane to prevent overflow from games with many players
        val scrollPane = addScrollPane(scoreTable)
        stage.addActor(scrollPane)

        // Add menu button

        val backToMenuButton = TextButton("MAIN MENU", skin)
        backToMenuButton.height = UIManager.elementHeight.toFloat()
        backToMenuButton.setPosition(UIManager.screenWidth / 2 - backToMenuButton.width / 2, screenHeight / 11.toFloat())
        backToMenuButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                controller.leaveLobby()
                gsm.push(MainMenuState(gsm))
            }

        })
        stage.addActor(backToMenuButton)
    }


    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}