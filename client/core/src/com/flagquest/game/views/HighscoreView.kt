package com.flagquest.game.views

import com.badlogic.gdx.graphics.Color
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
import com.flagquest.game.controllers.HighscoreController
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading

class HighscoreView(gsm: GameStateManager, private val stage: Stage, controller: HighscoreController) {
    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont
    private var players = mutableListOf<Pair<String, Int>>()
    private var friends = mutableListOf<Pair<String, Int>>()
    private var globalScores: Boolean = true
    private val globalBtn = TextButton("GLOBAL", skin)
    private val friendsBtn = TextButton("FRIENDS", skin)
    private val scoreTable = Table()
    private val btnTable = Table()
    private val standardColor = Color(globalBtn.color)
    private val highlightColor = Color(0.3882353f, 0.70980394f, 0.7764706f, 1f)
    private val backNavType = "menu"

    init {
        textFieldStyle.font.data.setScale(5f)

        // Add navigation elements
        addHeading(stage, "HIGHSCORE", 2.8f)
        addBackButton(stage, gsm, backNavType)

        // Initialise outer table element btnTable
        globalBtn.color = highlightColor
        btnTable.setFillParent(true)
        btnTable.center()
        btnTable.padBottom(850f)
        btnTable.add(globalBtn).width(400f).height(100f).pad(10f)
        btnTable.add(friendsBtn).width(400f).height(100f).pad(10f)
        btnTable.row()
        stage.addActor(btnTable)

        // Retrieve high score data
        scoreTable.setFillParent(true)
        players = controller.addGlobalHighscores().toMutableList()
        friends = controller.addFriendsHighscores()
        controller.addFriendsHighscores()

        // Add players to table
        for (player in players) {
            val labels = listOf(Label(player.first, skin), Label(player.second.toString(), skin))
            for (label in labels) {
                label.setStyle(Label.LabelStyle(titleFont, skin.getColor("textColor")))
                label.setFontScale(1f)
                scoreTable.add(label).align(Align.left).pad(10f).padLeft(35f)
            }
            scoreTable.row()
        }

        // Padding and adding to stage
        scoreTable.top().padTop(850f)
        stage.addActor(scoreTable)

        titleFont.data.setScale(1.2f)

        globalBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                globalScores = true
                globalBtn.color = highlightColor
                friendsBtn.color = standardColor
                updateList()
            }
        })

        friendsBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                globalScores = false
                friendsBtn.color = highlightColor
                globalBtn.color = standardColor
                updateList()
            }
        })
    }

    fun updateList() {
        scoreTable.clearChildren()

        val displayList = if (globalScores) players else friends
        for (user in displayList) {
            val labels = listOf(Label(user.first, skin), Label(user.second.toString(), skin))
            for (label in labels) {
                label.setStyle(Label.LabelStyle(titleFont, skin.getColor("textColor")))
                label.setFontScale(1f)
                scoreTable.add(label).align(Align.left).pad(10f).padLeft(35f)
            }
            scoreTable.row()
        }
    }

    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}