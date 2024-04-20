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
import com.flagquest.game.controllers.OnlineGameController
import com.flagquest.game.models.GameApiModel.AnswerOption
import com.flagquest.game.models.GameApiModel
import com.flagquest.game.models.GameApiModel.Question
import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.navigation.ResultRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager


class OnlineGameView(gsm: GameStateManager, private val stage: Stage, onlineGameListener: OnlineGameRedirectionListener, resultRedirectionListener: ResultRedirectionListener) {
    private val onlineGameController: OnlineGameController = OnlineGameController(GameApiModel(), LocalApiModel())
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) - 50).toFloat()
    private lateinit var currentQuestion: Question
    val answerButtons: MutableList<TextButton> = mutableListOf()

    init {
        val startTime = System.currentTimeMillis()

        onlineGameController.onlineGameRedirectionListener = onlineGameListener
        onlineGameController.resultRedirectionListener = resultRedirectionListener
        onlineGameController.attachNextRoundListener()
        onlineGameController.attachEndScoreListener()
        // val quiz: Quiz = controller.handleCreateOfflineGame(10, "Europe")
        currentQuestion = onlineGameController.getSingleQuestion()

        titleFont.data.setScale(1.5f)
        //Heading
        val headingY = screenHeight - 300f
        UIManager.addHeading(stage, "FLAGQUEST", 3.5f, posY = headingY)

        //Pause button
        val pauseBtnColor = Color(0.0235f, 0.24705f, 0.39607f, 1f)
        val pauseButtonSize = (screenHeight/11).toFloat()
        UIManager.addPauseButton(stage, gsm, pauseBtnColor, pauseButtonSize)

        //Flag
        var flagTex: Texture = Texture(Gdx.files.internal(onlineGameController.getFlagFilePathByCountryName(currentQuestion.description)))
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
                        onlineGameController.submitAnswer(true,
                            System.currentTimeMillis() - startTime
                        )
                        println("Correct!")
                    } else {
                        button.color = Color.RED
                        onlineGameController.submitAnswer(false,
                            System.currentTimeMillis() - startTime
                        )
                        println("Incorrect!")
                    }
                    disableButtons()
                    // TODO: Show the correct answer button in green
                    for (answerButton in answerButtons){
                        if (answerButton.text.toString() == currentQuestion.answerOptions.find { it.isCorrect }?.description){
                            answerButton.color = Color.GREEN
                        }
                    }
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
