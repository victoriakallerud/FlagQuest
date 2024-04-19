package com.flagquest.game

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val config = AndroidApplicationConfiguration()

        initialize(FlagQuest(FirebaseAuthHandler()), config)
    }
}
