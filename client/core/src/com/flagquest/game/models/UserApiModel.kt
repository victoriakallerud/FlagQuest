package com.flagquest.game.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

/**
 * Class handles API requests associated with User
 */
class UserApiModel {
    private val userId: String = "0e7cb4e7-c8db-41e7-b536-bf94c66c9e50" // TODO: Implement function to get user's id
    private val level: String = "Europe" // TODO Decide if we want to have Europe or All as our level

    fun postUser(name: String, username: String, nationality: String, password: String): String? {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = "{\r\n    \"userName\": \"$username\",\r\n    \"nationality\": \"$nationality\"\r\n}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/USER_ID")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function sends GET request to retrieve UserId with provided username
     * @param username of user
     * @return Server's response as a string
     */
    fun getUserByName(username: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/byName/$username")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }


    /**
     * Function sends GET request to retrieve User with provided userId
     * @param id of user
     * @return Server's response as a string
     */
    fun getUserById(id: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$id")
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
            .url("http://flagquest.leotm.de:3000/user/highScores/$level/GuessingFlags")
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function sends PUT request to send friend request to user with provided friendId
     * @param friendId userId of friend
     * @return Server's response as string
     */
    fun putAddFriend(friendId: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/0e7cb4e7-c8db-41e7-b536-bf94c66c9e50/friends/$friendId")
            .put(body)
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
        println(highscoreArray)

        for (i in 0 until highscoreArray.length()) {
            val jsonObject = highscoreArray.getJSONObject(i)
            val userName = jsonObject.getString("userName")
            val score = jsonObject.getInt("score")
            highscores.add(userName to score)
        }

        return highscores
    }

    /**
     * Function retrieves highscores of top 10 friends
     * @return List with pairs of username and score
     */
    fun getFriendNames(): MutableList<String> {
        val friendNames = mutableListOf<String>()
        val user = getUserById(userId)
        val friendUuidList = extractFriendUuidList(user!!)
        for (friendUuid in friendUuidList) {
            val friend = JSONObject(getUserById(friendUuid))
            friendNames.add(friend.getString("userName"))
        }
        return friendNames
    }

    /**
     * Function retrieves highscores of top 10 friends
     * @return List with pairs of username and score
     */
    fun getFriendHighscores(): MutableList<Pair<String, Int>> {
        val friendsScore = mutableListOf<Pair<String, Int>>()
        val user = getUserById(userId)
        val friendUuidList = extractFriendUuidList(user!!)

        for (friendUuid in friendUuidList) {
            val friend = JSONObject(getUserById(friendUuid))
            val highScoresArray = friend.optJSONArray("highScores")
            if (highScoresArray != null) {
                for (i in 0 until highScoresArray.length()) {
                    val scoreObject = highScoresArray.getJSONObject(i)
                    val scoreLevel = scoreObject.optString("level")
                    if (scoreLevel == level) {
                        val username: String = friend.optString("userName")
                        val userscore: Int = scoreObject.optInt("value")
                        friendsScore.add(username to userscore)
                    }
                }
            }
        }
        return friendsScore
    }

    /**
     * Function retrieves ID from user object
     * @param responseBody String of user object
     * @return ID of user
     */
    fun getIdFromResponse(responseBody: String): String {
        val jsonObject = JSONObject(responseBody)
        return jsonObject.getString("id")
    }


    /**
     * Function extracts friendUuidList from user object
     * @param jsonString String of user object
     * @return List of friendUuids
     */
    private fun extractFriendUuidList(jsonString: String): List<String> {
        val jsonObject = JSONObject(jsonString)
        val friendUuidJsonArray = jsonObject.getJSONArray("friendUuidList")
        val friendUuidList = mutableListOf<String>()

        for (i in 0 until friendUuidJsonArray.length()) {
            friendUuidList.add(friendUuidJsonArray.getString(i))
        }

        return friendUuidList
    }

    /**
     * Function sends GET request to retrieve pending friend requests
     * @return List of pairs with usernames and userIds of pending friend requests
     */
    fun getPendingFriendRequests(): MutableList<Pair<String, String>> {
        val pendingFriendRequests = mutableListOf<Pair<String, String>>()
        val jsonUser = JSONObject(getUserById(userId))
        val friendRequestIds = jsonUser.getJSONArray("pendingFriendRequests")
        for (i in 0 until friendRequestIds.length()) {
            val friendId = friendRequestIds.getString(i)
            val pendingFriendRequest = JSONObject(getUserById(friendId))
            pendingFriendRequests.add(pendingFriendRequest.getString("userName") to friendId)
        }
        return pendingFriendRequests
    }

    fun putAcceptRequest(friendId: String): String? {
        println("1")
        val client = OkHttpClient()
        println("2")
        val mediaType = "text/plain".toMediaType()
        println("3")
        val body = "".toRequestBody(mediaType)
        println("4")
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$userId/friends/requests/$friendId")
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        println("5")
        val response = client.newCall(request).execute()
        println("6")
        return response.body?.string()
    }

    fun delRejectRequest(friendId: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$userId/friends/requests/$friendId")
            .method("DELETE", body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }
}