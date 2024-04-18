package com.flagquest.game.models

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

/**
 * Class handles API requests associated with User
 */
class UserApiModel {
    private val userId: String = "0e7cb4e7-c8db-41e7-b536-bf94c66c9e50" // TODO: Implement function to get user's id

    /**
     * Function sends GET request to retrieve User with provided userId
     * @param userId of user
     * @return Server's response as a string
     */
    fun getUserById(userId: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/18b118ac-3dde-4285-975c-892885507559")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }


    /**
     * Function sends GET request to retrieve users with top 10 scores
     * @return Server's response as string
     */
    fun getTopTenUsers(): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/highScores/Europe/GuessingFlags")
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function takes in list of highscores and creates list with pairs of username and score
     * @return List with pairs of username and score
     */
    fun parseHighscores(highscoreString: String?): List<Pair<String, Int>> {
        val highscoreArray = JSONArray(highscoreString!!)
        val highscores = mutableListOf<Pair<String, Int>>()

        for (i in 0 until highscoreArray.length()) {
            val jsonObject = highscoreArray.getJSONObject(i)
            val userName = jsonObject.getString("userName")
            val score = jsonObject.getInt("score")
            highscores.add(userName to score)
        }

        return highscores
    }

    fun getAllFriends() {
        val friends = mutableListOf<Unit>()

    }
}