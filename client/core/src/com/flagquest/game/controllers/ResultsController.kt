package com.flagquest.game.controllers

import com.flagquest.game.models.UserApiModel

class ResultsController(private val model: UserApiModel) {
    fun addResults(): List<Pair<String, Int>> {
        var resultList: MutableList<Pair<String, Int>> = model.getResults()
        resultList.sortByDescending { it.second }
        return resultList
    }

}