package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.GameApiModel
import com.flagquest.game.models.GameApiModel.Question
import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.SocketHandler
import org.json.JSONObject


class OnlineGameController(private val gameModel: GameApiModel, private val localModel: LocalApiModel) {
    var redirectionListener: OnlineGameRedirectionListener? = null

    fun getSingleQuestion(): Question {
        return gameModel.getCurrentQuestion()
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
