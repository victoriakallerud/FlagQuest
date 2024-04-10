package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener

class GameLobbyState(gsm: GameStateManager, isAdmin: Boolean) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val stage = Stage(ScreenViewport())
    private val currParticipants: Int = 4 // TODO: Implement way of getting current participants number
    private val totalParticipants: Int = 6 // TODO: Implement way of getting total participants number

    private val heading = Label("GAME LOBBY", skin)
    private val codeText = Label("$currParticipants/$totalParticipants has joined", skin)
    private val names = arrayOf("Amel De Kok", "Felix Kuhn", "Leo Laisé", "Victoria Kallerud") // TODO: Implement way of getting participants

    init {
        Gdx.input.inputProcessor = stage
        textFieldStyle.font.data.setScale(5f)

        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(2.8f)
        heading.setAlignment(Align.center)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)

        codeText.setStyle(Label.LabelStyle(titleFont, codeText.style.fontColor))
        codeText.setFontScale(1.5f)
        codeText.pack()
        codeText.setPosition((screenWidth - codeText.prefWidth) / 2, pos + 250f)
        stage.addActor(codeText)

        val table = Table()
        table.setFillParent(true)

        for (name in names) {
            val nameLabel = Label(name, skin)
            nameLabel.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
            nameLabel.setFontScale(1f)
            table.add(nameLabel).pad(10f)
            table.row()
        }

        if (isAdmin) {
            val startButton = TextButton("START NOW", skin)
            startButton.height = buttonHeight.toFloat()
            startButton.setPosition(screenWidth / 2 - startButton.width / 2, 300f)
            startButton.addListener(ButtonClickListener(gsm,startButton.text.toString()))
            table.add(startButton).width(screenWidth * 0.8f).padTop(100f)
        }

        stage.addActor(table)

        titleFont.data.setScale(1.5f)

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