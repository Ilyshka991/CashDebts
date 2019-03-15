package com.pechuro.cashdebts.ui.activity.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pechuro.cashdebts.data.repositories.IAuthRepository
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    protected lateinit var authRepository: IAuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        super.onCreate(savedInstanceState)
        decideNextActivity()
    }

    private fun decideNextActivity() {
        val intent = if (authRepository.isUserSignedIn()) {
            MainActivity.newIntent(this)
        } else {
            AuthActivity.newIntent(this)
        }
        startActivity(intent)
        finish()
    }

    private fun performDI() = AndroidInjection.inject(this)
}
