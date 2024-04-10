package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener

class OnlineGameState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) - 150).toFloat()
    private val stage = Stage(ScreenViewport())

    private val countries = arrayOf("SLOVAKIA", "RUSSIA", "SLOVENIA", "CROATIA") //TODO: Retrieve from backend
    private val flagTex = Texture(Gdx.files.internal("slovenia.svg"))

    private val heading = Label("FLAGQUEST", skin)

    init {
        Gdx.input.inputProcessor = stage

        //Heading
        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(3.5f)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 300f)
        stage.addActor(heading)
        titleFont.data.setScale(1.5f)

        //Flag
        val flagImg = Image(flagTex)
        flagImg.setPosition((screenWidth - flagImg.prefWidth)/2, screenHeight - 800f)
        stage.addActor(flagImg)

        for (country in countries) {
            val button = TextButton(country, skin)
            button.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.setPosition(screenWidth / 2 - button.width / 2, pos)
            button.addListener(ButtonClickListener(gsm,button.text.toString()))
            stage.addActor(button)
            pos -= button.height + 30
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