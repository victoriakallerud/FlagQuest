package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener

import okhttp3.OkHttpClient
import okhttp3.Request
import com.flagquest.game.utils.UIManager.addNavButtonArray
import com.flagquest.game.utils.UIManager.addHeading

class MainMenuState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    override val stage = Stage(ScreenViewport())
    private val buttonStartingPos = ((screenHeight / 2) + 150).toFloat()
    override var backNavType = "nothing"

    private val buttons = arrayOf(
        TextButton("CREATE GAME", skin) to lazy { LobbyInitiationState(gsm) },
        TextButton("JOIN GAME", skin) to lazy { JoinGameState(gsm) },
        TextButton("TRAINING MODE", skin) to lazy { OnlineGameState(gsm) }, //TODO: Link to OFFLINE state upon implementation
        TextButton("HIGHSCORE BOARD", skin) to lazy { HighscoreState(gsm) },
        TextButton("MANAGE FRIENDS", skin) to lazy { ManageFriendsState(gsm) }
    )

    init {
        Gdx.input.inputProcessor = stage
        addHeading(stage, "FLAGQUEST", fontScale = 3.5f)
        addNavButtonArray(stage, gsm, buttons, buttonStartingPos)
        titleFont.data.setScale(1.5f)

        // Test get request, get Lunitik user with id 398315ed-3e05-47dd-ac50-37d1fbe441d9
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/398315ed-3e05-47dd-ac50-37d1fbe441d9")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        println(response.body?.string())
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
