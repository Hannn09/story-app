package com.example.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.DetailResponse
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.Story
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.utils.ResultState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class StoryViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _detailResult = MutableLiveData<ResultState<DetailResponse>>()
    val detailResult: LiveData<ResultState<DetailResponse>> = _detailResult

    val list: LiveData<StoryResponse> = userRepository.list
    val isLoading: LiveData<Boolean> = userRepository.isLoading

    fun getSession() = userRepository.getSession().asLiveData()
    fun uploadImage(token: String, file: File, description: String) = userRepository.insertStories(token, file, description)

    fun getDetail(token: String, id: String) {
        viewModelScope.launch {
            userRepository.getDetailStory(token, id).asFlow().collect{
                _detailResult.value = it
            }
        }
    }

    fun listStoryLocation(token: String) {
        viewModelScope.launch {
            userRepository.getListStoryWithLocation(token)
        }
    }





}