package com.flagquest.game.models

import com.flagquest.game.states.GameLobbyState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
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
        return response.body?.string()
    }

    /**
     * Function sends PUT request to join a lobby with an invite code
     * @param inviteCode six digit invite code
     * @return Server's response as string
     */
    fun putLobbyWithInviteCode(inviteCode: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/invite/$inviteCode/$userId") // lobbyId/userId TODO: Add functionality to use the device's user's ID
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function sends PUT request to join a lobby with a lobbyId
     */
    fun putLobbyWithId(id: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/$id/$userId" )// lobbyId/userId TODO: Add functionality to use the device's user's ID
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function sends GET request to retrieve all lobbies
     * @return Server's response as string
     */
    fun getAllLobbies(): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function extracts and returns a lobby's ID
     * @param responseBody Server's response as string in JSON format
     * @return Lobby's ID
     */
    fun getIdFromResponse(responseBody: String): String {
        val jsonObject = JSONObject(responseBody)
        println("JsonObject: $jsonObject")
        println("ID: ${jsonObject.getString("id")}")
        return jsonObject.getString("id")
    }

    /**
     * Function extracts first lobby from lobby list
     * @param lobbyList List of all lobbies
     * @return First lobby in list
     */
    fun getFirstLobby(lobbyList: String): String? {
        val jsonArray = JSONArray(lobbyList)
        if (jsonArray.length() > 0) {
            val firstLobby = jsonArray.getJSONObject(0)
            return firstLobby.toString()
        }
        return null
    }
}

