package com.example.storyapp.ui.home

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
import com.example.storyapp.ui.map.MapsActivity
import com.example.storyapp.ui.story.add.AddStoryActivity
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel by viewModels<LoginViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Log.d(TAG, "onCreate: ${user.isLogin}")
                showStory()
                val adapter = StoryAdapter()
                loginViewModel.getListStory.observe(this) {
                    adapter.submitData(lifecycle, it)
                }
                binding.rvStory.adapter = adapter
            }
        }

        binding.btnLogout.setOnClickListener {
            loginViewModel.logout()
        }

        binding.btnMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
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