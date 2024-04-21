package com.flagquest.game.controllers

import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.models.Question
import com.flagquest.game.navigation.TrainingQuestionRedirectionListener

class OfflineGameController(private val model: LocalApiModel) {
    var redirectionListener: TrainingQuestionRedirectionListener? = null

    fun getSingleQuestion(): Question {
        return model.generateQuestion()
    }

    fun getFlagFilePathByCountryName(countryName: String): String {
        return model.getFlagFilePathByCountryName(countryName)
    }
}
