package com.flagquest.game.utils

object DataManager {
    private val data: MutableMap<String, Any> = mutableMapOf()

    fun setData(key: String, value: Any) {
        data[key] = value
    }

    fun getData(key: String): Any? {
        return data[key]
    }
}