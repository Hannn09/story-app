package com.example.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val userRepository: UserRepository): ViewModel() {

    val listStory: LiveData<List<ListStoryItem>> = userRepository.listStory
    val detailData: LiveData<Story> = userRepository.detailData
    val isLoading: LiveData<Boolean> = userRepository.isLoading

    private val _uploadStoryResult = MutableLiveData<Pair<Boolean, String>>()
    val uploadStoryResult: LiveData<Pair<Boolean, String>> = _uploadStoryResult

    fun getAllStory() {
        viewModelScope.launch {
            userRepository.getListStory()
            Log.d("StoryViewModel", "getAllStory: masuk!")
        }
    }


    fun getDetailStoryUser(id: String) {
        viewModelScope.launch {
            userRepository.getDetailStory(id)
        }
    }

    fun uploadStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                userRepository.insertStories(file, description)
                _uploadStoryResult.value = Pair(true, "Upload Story Successful!")
            } catch (e: Exception) {
                _uploadStoryResult.value = Pair(false, "Upload Story Failed!")
            }
        }


    }
}