package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.GameApiModel
import com.flagquest.game.models.GameApiModel.Question
import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.navigation.ResultRedirectionListener
import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.SocketHandler
import org.json.JSONArray
import org.json.JSONObject


class OnlineGameController(private val gameModel: GameApiModel, private val localModel: LocalApiModel) {
    var onlineGameRedirectionListener: OnlineGameRedirectionListener? = null
    var resultRedirectionListener: ResultRedirectionListener? = null

    fun getSingleQuestion(): Question {
        return gameModel.getCurrentQuestion()
    }

    fun attachNextRoundListener() {
        SocketHandler.getSocket().on("nextRound") { args ->
            var message = args[0] as Int
            message -= 1
            DataManager.setData("currentQuestionIndex", message)
            Gdx.app.log("GameApiModel", "nextRound: $message")
            Gdx.app.postRunnable {
                detachNextRoundListener()
                onlineGameRedirectionListener?.redirectToOnlineGameState()
            }
        }
    }

    fun attachEndScoreListener() {
        SocketHandler.getSocket().on("endScore") { args ->
            val message = args[0] as JSONArray
            DataManager.setData("endScore", message)
            Gdx.app.log("GameApiModel", "endScore: $message")
            Gdx.app.postRunnable {
                resultRedirectionListener?.redirectToResultState()
            }
        }
    }

    private fun detachNextRoundListener() {
        SocketHandler.getSocket().off("nextRound")
    }

    fun submitAnswer(isAnswerRight: Boolean, answerTime: Int) {
        val data = JSONObject()
        data.put("lobbyId", DataManager.getData("lobbyId"))
        data.put("playerId", DataManager.getData("userId"))
        data.put("isAnswerRight", isAnswerRight)
        data.put("answerTime", answerTime)
        Gdx.app.log("OnlineGameController", "submitAnswer: $data")
        SocketHandler.emit("submitAnswer", data)
    }

    fun getFlagFilePathByCountryName(countryName: String): String {
        return localModel.getFlagFilePathByCountryName(countryName)
    }
}
