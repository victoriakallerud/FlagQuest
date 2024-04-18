package com.flagquest.game.controllers

import com.flagquest.game.models.LocalApiModel
import com.flagquest.game.models.Question
import com.flagquest.game.models.Quiz
import com.flagquest.game.navigation.TrainingQuestionRedirectionListener

class OfflineGameController(private val model: LocalApiModel) {
//    fun handleCreateOfflineGame(numberOfQuestions: Int, region: String): Quiz {
//        return model.generateQuiz(numberOfQuestions, region)
//    }

    var redirectionListener: TrainingQuestionRedirectionListener? = null

    fun getSingleQuestion(region: String): Question {
        return model.generateQuestion(region)
    }

    fun getFlagFilePathByCountryName(countryName: String): String {
        return model.getFlagFilePathByCountryName(countryName)
    }
}