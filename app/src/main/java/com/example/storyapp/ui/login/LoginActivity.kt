package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.data.local.entity.UsersEntity
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.MainActivity
import com.example.storyapp.utils.SettingsPreferences
import com.example.storyapp.utils.SharedPreference
import com.example.storyapp.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefs : SharedPreference
    private val loginViewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val emailEditText = binding.emailEditText
        emailEditText.setText("almas@gmail.com")
        val passwordEditText = binding.passwordEditText
        passwordEditText.setText("12345678")

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                showErrorDialog("Fill it All First!")
            }

        }

        loginViewModel.isLogin.observe(this) { userLogin ->
            if (userLogin) {
                navigateToMainPage()
            } else {
                showErrorDialog("Username or Password is Wrong!")
                Log.d("LoginActivity", "Username or Password is Wrong!")
            }
        }

    }

    private fun playAnimation() {
        val welcome = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(100)
        val story = ObjectAnimator.ofFloat(binding.story, View.ALPHA, 1f).setDuration(100)
        val image = ObjectAnimator.ofFloat(binding.image, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(100)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(100)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val acc = ObjectAnimator.ofFloat(binding.tvHaveAcc, View.ALPHA, 1f).setDuration(100)
        val register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                welcome,
                story,
                image,
                email,
                emailEdit,
                password,
                passwordEdit,
                btnLogin,
                acc,
                register
            )
            startDelay = 200
        }.start()

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error!")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}