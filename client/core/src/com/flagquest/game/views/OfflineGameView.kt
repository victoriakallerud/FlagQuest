package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Timer
import com.flagquest.game.controllers.OfflineGameController
import com.flagquest.game.models.AnswerOption
import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.models.Question
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.navigation.TrainingQuestionRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.OfflineGameState
import com.flagquest.game.utils.UIManager
import org.w3c.dom.Text


class OfflineGameView(gsm: GameStateManager, private val stage: Stage, listener: TrainingQuestionRedirectionListener, fromLogin: Boolean) {
    private val controller: OfflineGameController = OfflineGameController(LocalApiModel())
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) - 50).toFloat()
    private lateinit var currentQuestion: Question
    val answerButtons: MutableList<TextButton> = mutableListOf()

    init {
        controller.redirectionListener = listener
        // val quiz: Quiz = controller.handleCreateOfflineGame(10, "Europe")
        currentQuestion = controller.getSingleQuestion("Europe")

        titleFont.data.setScale(1.5f)
        //Heading
        val headingY = screenHeight - 300f
        UIManager.addHeading(stage, "FLAGQUEST", 3.5f, posY = headingY)

        //Back button
        if (fromLogin) {
            UIManager.addBackButton(stage, gsm, "login")
        } else {
            UIManager.addBackButton(stage, gsm, "menu")
        }

        //Flag
        var flagTex: Texture = Texture(Gdx.files.internal(controller.getFlagFilePathByCountryName(currentQuestion.description)))
        var flagImg = Image(flagTex)
        flagImg.setPosition((screenWidth - flagImg.prefWidth)/2, screenHeight - 800f)
        stage.addActor(flagImg)

        for (answerOption: AnswerOption in currentQuestion.answerOptions) {
            val button = TextButton(answerOption.description, skin)
            button.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.setPosition(screenWidth / 2 - button.width / 2, pos)
            answerButtons.add(button)

            button.addListener(object: ClickListener(){
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if(answerOption.isCorrect){
                        button.color = Color.GREEN
                        println("Correct!")
                    } else {
                        button.color = Color.RED
                        println("Incorrect!")
                    }
                    disableButtons()
                    // Wait for 2 seconds before moving to the next question
                    Timer.schedule(object: Timer.Task() {
                        override fun run() {
                            controller.redirectionListener?.redirectToNewTrainingQuestion()
                        }
                    }, 2f)
                    // Move to the next question
                    currentQuestion = controller.getSingleQuestion("Europe")
                }
            })
            stage.addActor(button)
            pos -= button.height + 30
            }
        }
    private fun disableButtons(){
        for (button in answerButtons){
            // Remove the listeners
            button.clearListeners()
        }
    }

    private fun enableButtons(){
        for (button in answerButtons){
            button.isDisabled = false
        }
    }

}
