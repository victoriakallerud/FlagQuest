package com.flagquest.game.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


/**
 * Class handles API requests associated with Lobby
 */
class LobbyApiModel {
    private val userId: String = "0e7cb4e7-c8db-41e7-b536-bf94c66c9e50" // TODO: Implement function to get user's id

    /**
     * Function sends POST request to create a new lobby
     * @param size Maximum number of players allowed in the lobby
     * @return Server's response as string
     */
    fun postLobby(size: Int): String? {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = ("{" +
                "\r\n  \"name\": \"12345\"," +
                "\r\n  \"admin\": \"$userId\"," +
                "\r\n  \"options\": " +
                "{\r\n    \"maxNumOfPlayers\": \"$size\"," +
                "\r\n    \"numberOfQuestions\": 10," +
                "\r\n    \"showAnswers\": true," +
                "\r\n    \"gameMode\": \"GuessingFlags\"," +
                "\r\n    \"level\": \"Europe\"," +
                "\r\n    \"isPrivate\": false" +
                "\r\n  }\r\n" +
                "}").toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        println(response.body?.string())
        return response.body?.string()
    }

    /**
     * Function extracts and returns a lobby's ID
     * @param responseBody Server's response as string in JSON format
     * @return Lobby's ID
     */
    fun getIdFromResponse(responseBody: String): String {
        val jsonObject = JSONObject(responseBody)
        return jsonObject.getString("id")
    }
}