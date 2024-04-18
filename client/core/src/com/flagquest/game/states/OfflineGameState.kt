package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.UIManager.addPauseButton
import com.flagquest.game.utils.SocketHandler

// Use without arguments when first presenting the quiz. Load with chosen and correct answer to reveal.
class OfflineGameState(gsm: GameStateManager, chosen: String? = null, correct: String? = null) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) - 50).toFloat()
    override val stage = Stage(ScreenViewport())
    override var backNavType = "pause"

    private val countries = arrayOf("SLOVAKIA", "RUSSIA", "SLOVENIA", "CROATIA") //TODO: Retrieve from backend
    private val correctCountry = "SLOVENIA" //TODO: Retrieve from backend
    private val flagTex = Texture(Gdx.files.internal("slovenia.svg"))


    init {
        titleFont.data.setScale(1.5f)
        //Heading
        val headingY = screenHeight - 300f
        addHeading(stage,"FLAGQUEST", 3.5f, posY = headingY)

        //Pause button
        val pauseBtnColor = Color(0.0235f, 0.24705f, 0.39607f, 1f)
        val pauseButtonSize = (screenHeight/11).toFloat()
        addPauseButton(stage, gsm, pauseBtnColor, pauseButtonSize)

        //Flag
        val flagImg = Image(flagTex)
        flagImg.setPosition((screenWidth - flagImg.prefWidth)/2, screenHeight - 800f)
        stage.addActor(flagImg)

        for (country in countries) {
            val button = TextButton(country, skin)
            button.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.setPosition(screenWidth / 2 - button.width / 2, pos)

            if (chosen == null){ // Only let the person guess if they haven't before.
                button.addListener(object: ClickListener(){
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        gsm.push(OfflineGameState(gsm, chosen = country, correct=correctCountry))
                    }
                })
            }

            if (country == chosen && country != correct)
                button.setColor(.892f,.393f,.37f,1f)
            if (country == correct)
                button.setColor(0.286F, 0.612F, 0.384F,1f)

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
