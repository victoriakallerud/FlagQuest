package com.flagquest.game.views

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager

class PauseView(gsm: GameStateManager, private val stage: Stage) {
    private val skin: Skin = UIManager.skin
    private val titleFont: BitmapFont = UIManager.titleFont
    private val screenHeight = UIManager.screenHeight
    private var pos: Float = ((screenHeight / 2) - 150).toFloat()

    private val resumeBtn = TextButton("RESUME", skin) to "back"
    private val menuBtn = TextButton("MAIN MENU", skin) to "menu"
    private val buttons = arrayOf(resumeBtn, menuBtn)

    init {
        titleFont.data.setScale(1.5f)
        UIManager.addHeading(stage, "GAME PAUSED", 2.8f)
        resumeBtn.first.setColor(0.286F, 0.612F, 0.384F,1f)
        menuBtn.first.setColor(.892f,.393f,.37f,1f)
        UIManager.addInstructButtonArray(stage, gsm, buttons, pos)
    }
}