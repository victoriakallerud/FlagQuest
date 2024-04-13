package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.UIManager.addTextButton
import com.flagquest.game.utils.UIManager.addTextButtonArray
import com.flagquest.game.utils.UIManager.addTextButtonArrayString
import com.flagquest.game.utils.UIManager.addTextButtonString

class PauseState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    val titleFont: BitmapFont = skin.getFont("title")
    var screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height
    var pos: Float = ((screenHeight / 2) - 150).toFloat()
    override val stage = Stage(ScreenViewport())

    val resumeBtn = TextButton("RESUME", skin) to "back"
    val menuBtn = TextButton("MAIN MENU", skin) to "menu"
    val buttons = arrayOf(resumeBtn, menuBtn)

    init {
        titleFont.data.setScale(1.5f)
        addHeading(stage,"GAME PAUSED",2.8f)
        resumeBtn.first.setColor(0.286F, 0.612F, 0.384F,1f)
        menuBtn.first.setColor(.892f,.393f,.37f,1f)
        addTextButtonArrayString(stage,gsm,buttons,pos)
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