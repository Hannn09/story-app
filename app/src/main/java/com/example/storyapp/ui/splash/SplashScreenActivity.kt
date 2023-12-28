package com.example.storyapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.ui.home.MainActivity
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.utils.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME: Long = 2000
    private val loginViewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        @Suppress("DEPRECATION")
        Handler().postDelayed({
          checkSession()
        }, SPLASH_TIME)
    }

    private fun checkSession() {
        loginViewModel.getSession().observe(this) {
            if (!it.isLogin) {
                navigateToLogin()
            } else {
                navigateToMain()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}