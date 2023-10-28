package com.example.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.StoryAdapter
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.ui.story.add.AddStoryActivity
import com.example.storyapp.utils.SettingsPreferences
import com.example.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storyViewModel by viewModels<StoryViewModel> { ViewModelFactory.getInstance(this) }
    private val loginViewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        storyViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.getSaveToken().observe(this) {
            Log.d(TAG, "GET TOKEN : $it")
        }

        storyViewModel.getAllStory()

        storyViewModel.listStory.observe(this) { listStoryItem ->
            Log.d(TAG, "List Data : $listStoryItem")
            if (listStoryItem.isNotEmpty()) {
                binding.rvStory.adapter = StoryAdapter(listStoryItem)
                showStory()
            } else {
                Toast.makeText(this, "Data  Story Kosong!", Toast.LENGTH_SHORT).show()
            }
        }


        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            loginViewModel.logout()
            navigateToLogin()
        }

    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showStory() {
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}