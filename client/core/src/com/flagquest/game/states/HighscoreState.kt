package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
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
import com.badlogic.gdx.utils.viewport.ScreenViewport

class HighscoreState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val stage = Stage(ScreenViewport())
    private val heading = Label("HIGHSCORE", skin)
    private var players = arrayOf( // TODO: Implement way of getting the 10 players with the highest ranking
        Pair("Amel De Kok", 10369),
        Pair("Sindre Havn", 7388),
        Pair("Felix Kuhn", 10354),
        Pair("Magnus Lien", 7824),
        Pair("Håvar Hagelund", 10036),
        Pair("Urban Mikic", 8319),
        Pair("Leo Laisé", 9653),
        Pair("Hanna Lunne", 8524),
        Pair("Nina Tran", 9301),
        Pair("Victoria Kallerud", 8984)
    )
    private var friends = arrayOf( // TODO: Implement way of getting the 10 friends with the highest ranking
        Pair("Amel De Kok", 10369),
        Pair("Leo Laisé", 9653),
        Pair("Felix Kuhn", 10354),
        Pair("Victoria Kallerud", 8984)
    )
    private var globalScores: Boolean = true

    private val globalBtn = TextButton("GLOBAL", skin)
    private val friendsBtn = TextButton("FRIENDS", skin)
    val scoreTable = Table()

    init {
        Gdx.input.inputProcessor = stage
        textFieldStyle.font.data.setScale(5f)

        players = players.sortedByDescending { it.second }.toTypedArray()
        friends = friends.sortedByDescending { it.second }.toTypedArray()

        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(2.8f)
        heading.setAlignment(Align.center)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)

        scoreTable.setFillParent(true)

        val btnTable = Table()
        btnTable.setFillParent(true)
        btnTable.center()
        btnTable.padBottom(850f)

        btnTable.add(globalBtn).width(400f).height(100f).pad(10f)
        btnTable.add(friendsBtn).width(400f).height(100f).pad(10f)
        btnTable.row()
        stage.addActor(btnTable)


        val displayList = if (globalScores) players else friends

        for (user in displayList) {
            val nameLabel = Label(user.first, skin)
            nameLabel.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
            nameLabel.setFontScale(1f)
            scoreTable.add(nameLabel).align(Align.left).pad(10f).padRight(35f)
            val scoreLabel = Label(user.second.toString(), skin)
            scoreLabel.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
            scoreLabel.setFontScale(1f)
            scoreTable.add(scoreLabel).align(Align.left).pad(10f).padLeft(35f)
            scoreTable.row()
        }

        scoreTable.top().padTop(800f)
        stage.addActor(scoreTable)

        titleFont.data.setScale(1.2f)

        val standardColor: Color = Color(globalBtn.color)
        val highlightColor: Color = Color(0.3882353f, 0.70980394f, 0.7764706f, 1f)

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
            val nameLabel = Label(user.first, skin)
            nameLabel.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
            nameLabel.setFontScale(1f)
            scoreTable.add(nameLabel).align(Align.left).pad(10f).padRight(35f)
            val scoreLabel = Label(user.second.toString(), skin)
            scoreLabel.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
            scoreLabel.setFontScale(1f)
            scoreTable.add(scoreLabel).align(Align.left).pad(10f).padLeft(35f)
            scoreTable.row()
        }
    }

    override fun handleInput() {
        screenWidth = screenWidth // Just placeholder code to make the code compile
    }
    override fun update(dt: Float) {
        handleInput()
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}