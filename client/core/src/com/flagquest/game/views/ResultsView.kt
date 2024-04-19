package com.flagquest.game.views

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.flagquest.game.controllers.ResultsController
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.UIManager.addScrollPane
import com.flagquest.game.utils.UIManager.screenHeight
import com.flagquest.game.utils.UIManager.screenWidth

class ResultsView(gsm: GameStateManager, private val stage: Stage) {
    val controller: ResultsController = ResultsController(UserApiModel())

    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont

    private val heading = Label("HIGHSCORE", skin)

    private var results = mutableListOf<Pair<String, Int>>()
    private val scoreTable = Table()
    private val backNavType = "menu"

    init {
        textFieldStyle.font.data.setScale(5f)

        addHeading(stage, "RESULTS", 2.8f)
        addBackButton(stage, gsm, backNavType)

        // Styling
        scoreTable.setSize((screenWidth/100*80).toFloat(), 700f)
        val panePosY = (screenHeight - scoreTable.height)/2f + 100f
        scoreTable.setPosition((screenWidth - scoreTable.width)/2f, panePosY)

        //scoreTable.setFillParent(true)
        results = controller.addResults().toMutableList()

        var ranking = 1
        for (player in results) {
            val labels = listOf(Label(ranking.toString(), skin),Label(player.first, skin), Label(player.second.toString(), skin))
            for (label in labels) {
                label.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
                label.setFontScale(1.8f)
                scoreTable.add(label).align(Align.left).pad(10f).padLeft(35f)
            }
            scoreTable.row()
            ranking++
        }

        val scrollPane = addScrollPane(scoreTable)

        stage.addActor(scrollPane)

        titleFont.data.setScale(1.2f)
    }


    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}