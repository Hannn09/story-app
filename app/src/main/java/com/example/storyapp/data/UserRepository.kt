package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.example.storyapp.data.model.SessionModel
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.data.remote.retrofit.ApiService
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class UserRepository private constructor(
    private val pref: SettingsPreferences,
    private val apiService: ApiService,
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _list = MutableLiveData<StoryResponse>()
    val list: LiveData<StoryResponse> = _list

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun setLoginUser(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getListStory(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStories("Bearer $token")
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error("${e.message}"))
        }
    }

    fun getListStoryWithLocation(token: String) {
        _isLoading.value = true
        val client = apiService.getStoriesWithLocation("Bearer $token")
        client.enqueue(object : retrofit2.Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                try {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _list.value = response.body()
                    } else {
                        Log.d(TAG, "onResponse: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error Loaded: ${e.message.toString()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }

        })

    }

    suspend fun getDetailStory(token: String, id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getDetailStories("Bearer $token", id)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error("${e.message}"))
        }
    }

    fun insertStories(token: String, imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse =
                apiService.insertStories("Bearer $token", multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: Exception) {
            emit(ResultState.Error("${e.message}"))
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(config = PagingConfig(
            pageSize = 5
        ), pagingSourceFactory = {
            StoryPagingSource(pref, apiService)
        }).liveData
    }

    suspend fun saveSession(user: SessionModel) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<SessionModel> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.deleteToken()
    }


    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            pref: SettingsPreferences,
            apiService: ApiService,
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(pref, apiService)
        }.also { instance = it }
    }


}