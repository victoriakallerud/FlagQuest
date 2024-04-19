package com.flagquest.game.models

import com.badlogic.gdx.Gdx
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.OnlineGameState
import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.SocketHandler
import org.json.JSONArray
import org.json.JSONObject

class GameApiModel() {
    data class Question (val description: String, val answerOptions: List<AnswerOption>)
    data class AnswerOption (val description: String, val isCorrect: Boolean)
    data class Quiz (val questions: List<Question>)

    private val answerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/answer.mp3"))

    fun joinGameOnSocket() {
        SocketHandler.setSocket()
        // SocketHandler.addAllListeners()
        SocketHandler.getSocket().on("connection") {
            Gdx.app.log("GameApiModel", "Connected to WebSocket")
        }
        SocketHandler.getSocket().on("status") { args ->
            val message = if (args[0] is String) JSONObject(args[0] as String) else args[0] as JSONObject
            Gdx.app.log("GameApiModel", "status: $message")
        }
        SocketHandler.getSocket().on("updateLobby") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("GameApiModel", "updateLobby: $message")
        }

        SocketHandler.getSocket().on("answerCount") { args ->
            val message = args[0] as Int
            DataManager.setData("answerCount", message)
            answerSound.play()

            Gdx.app.log("GameApiModel", "answerCount: $message")
        }
        SocketHandler.getSocket().on("nextRound") { args ->
            var message = args[0] as Int
            message -= 1
            DataManager.setData("currentQuestionIndex", message)
            Gdx.app.log("GameApiModel", "nextRound: $message")
        }
        SocketHandler.getSocket().on("endScore") { args ->
            val message = args[0] as JSONArray
            Gdx.app.log("GameApiModel", "endScore: $message")
        }
        SocketHandler.establishConnection()
        val data = JSONObject()
        data.put("lobbyId", DataManager.getData("lobbyId"))
        data.put("userId", DataManager.getData("userId"))

        SocketHandler.emit("joinLobby", data)
    }

    fun getCurrentQuestion(): Question {
        // Get current question
        val questions = DataManager.getData("questions") as JSONArray
        val currentQuestionIndex = DataManager.getData("currentQuestionIndex") as Int
        Gdx.app.log("GameApiModel", "Getting current question at index $currentQuestionIndex")
        Gdx.app.log("GameApiModel", "questions are now set to: $questions")
        val question = questions.getJSONObject(currentQuestionIndex)
        val description = question.getString("description")
        val answerOptions = question.getJSONArray("answerOptions")
        val answerOptionsList = mutableListOf<AnswerOption>()
        for (i in 0 until answerOptions.length()) {
            val answerOption = answerOptions.getJSONObject(i)
            answerOptionsList.add(AnswerOption(answerOption.getString("answer"), answerOption.getBoolean("isCorrect")))
        }
        return Question(description, answerOptionsList)
    }
}