package com.pechuro.cashdebts.ui.activity.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        decideNextActivity()
    }

    private fun decideNextActivity() {
        val intent = if (FirebaseAuth.getInstance().currentUser != null) {
            MainActivity.newIntent(this)
        } else {
            AuthActivity.newIntent(this)
        }
        startActivity(intent)
        finish()
    }
}
