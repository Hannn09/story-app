package com.example.storyapp.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> { ViewModelFactory.getInstance(this) }

    companion object {
        private const val TAG = "DetailStoryActivity"
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getStringExtra(EXTRA_USER).toString()
        storyViewModel.getSession().observe(this) { user ->
            storyViewModel.getDetail(user.token, detail)
        }

        storyViewModel.detailResult.observe(this) { result ->
            if (result != null) {
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Success -> {
                        binding.apply {
                            Glide.with(this@DetailStoryActivity).load(result.data.story?.photoUrl).into(imageStory)
                            description.text = result.data.story?.description
                            showLoading(false)
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}