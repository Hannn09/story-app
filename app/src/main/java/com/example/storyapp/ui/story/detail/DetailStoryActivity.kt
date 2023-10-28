package com.example.storyapp.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.story.StoryViewModel
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
         storyViewModel.getDetailStoryUser(detail)


        storyViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        storyViewModel.detailData.observe(this) { detailStory ->
            binding.apply {
                Glide.with(this@DetailStoryActivity)
                    .load(detailStory.photoUrl)
                    .into(imageStory)
                username.text = detailStory.name
                Log.d(TAG, "name : ${detailStory.name}")
                description.text = detailStory.description
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}