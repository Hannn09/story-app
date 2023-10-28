package com.example.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.data.local.entity.UsersEntity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> { ViewModelFactory.getInstance(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.registrationResult.observe(this) {
            val (isSuccess, message) = it
            if (isSuccess) {
                showSuccessDialog(message)
            } else {
                showErrorDialog(message)
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val users = UsersEntity(email, name, password)

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                registerViewModel.register(users)
            } else {
                showErrorDialog("Fill it All First!")
            }

            playAnimation()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val create = ObjectAnimator.ofFloat(binding.create, View.ALPHA, 1f).setDuration(100)
        val story = ObjectAnimator.ofFloat(binding.story, View.ALPHA, 1f).setDuration(100)
        val image = ObjectAnimator.ofFloat(binding.image, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.name, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val acc = ObjectAnimator.ofFloat(binding.acc, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(create, story, image, email, emailEdit, name, nameEdit, password, passwordEdit,btnRegister, acc, login)
            startDelay = 200
        }.start()

    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Success!")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                clearInput()
                navigateToLoginActivity()
            }
            .create()
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error!")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                clearInput()
            }
            .create()
            .show()
    }

    private fun clearInput() {
        binding.emailEditText.text?.clear()
        binding.nameEditText.text?.clear()
        binding.passwordEditText.text?.clear()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}