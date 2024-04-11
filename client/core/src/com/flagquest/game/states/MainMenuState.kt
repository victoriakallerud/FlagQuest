package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener

class MainMenuState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val stage = Stage(ScreenViewport())

    private val heading = Label("FLAGQUEST", skin)
    private val createBtn = TextButton("CREATE GAME", skin)
    private val joinBtn = TextButton("JOIN GAME", skin)
    private val trainingBtn = TextButton("TRAINING MODE", skin)
    private val highscoreBtn = TextButton("HIGHSCORE BOARD", skin)
    private val friendsBtn = TextButton("MANAGE FRIENDS", skin)

    private val buttons = arrayOf(
        createBtn to lazy { LobbyInitiationState(gsm) },
        joinBtn to lazy { JoinGameState(gsm) },
        trainingBtn to lazy { OnlineGameState(gsm) }, //TODO: Link to OFFLINE state upon implementation
        highscoreBtn to lazy { HighscoreState(gsm) },
        friendsBtn to lazy { ManageFriendsState(gsm) }
    )

    init {
        Gdx.input.inputProcessor = stage
        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(3.5f)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)
        titleFont.data.setScale(1.5f)
        for (button in buttons) {
            button.first.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.first.setPosition(screenWidth / 2 - button.first.width / 2, pos)
            button.first.addListener(ButtonClickListener(gsm,button.second))
            stage.addActor(button.first)
            pos -= button.first.height + 30
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