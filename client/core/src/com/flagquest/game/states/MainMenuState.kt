package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addTextButtons
import com.flagquest.game.utils.UIManager.addHeading

class MainMenuState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    override val stage = Stage(ScreenViewport())
    private val buttonStartingPos = ((screenHeight / 2) + 150).toFloat()
    private val heading = Label("FLAGQUEST", skin)

    private val buttons = arrayOf(
        TextButton("CREATE GAME", skin) to lazy { LobbyInitiationState(gsm) },
        TextButton("JOIN GAME", skin) to lazy { JoinGameState(gsm) },
        TextButton("TRAINING MODE", skin) to lazy { OnlineGameState(gsm) }, //TODO: Link to OFFLINE state upon implementation
        TextButton("HIGHSCORE BOARD", skin) to lazy { HighscoreState(gsm) },
        TextButton("MANAGE FRIENDS", skin) to lazy { ManageFriendsState(gsm) }
    )

    init {
        Gdx.input.inputProcessor = stage
        drawTitle()
        addHeading(stage, "FLAGQUEST", fontScale = 3.5f)
        addTextButtons(stage, gsm, buttons, buttonStartingPos)
        addBackButton(stage, gsm)
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

    private fun drawTitle(){
        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(3.5f)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        titleFont.data.setScale(1.5f)
        stage.addActor(heading)
    }

}
