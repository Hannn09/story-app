package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.data.model.SessionModel
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.home.MainActivity
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        playAnimation()
        setupAction()

        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.emailEditText.setText("almas@gmail.com")
        binding.passwordEditText.setText("12345678")
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            showLoading(true)

            loginViewModel.login(email, password)
            loginViewModel.account.observe(this){ user->
                if (user != null){
                    when (user) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            loginViewModel.setupAction(SessionModel(email, user.data.loginResult?.token!!))
                            showLoading(false)
                            navigateToMainPage()
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                        }
                    }
                }
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
            startDelay = 100
        }.start()

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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