package com.example.storyapp.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.local.entity.UsersEntity
import com.example.storyapp.data.local.room.UsersDao
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.Story
import com.example.storyapp.data.remote.retrofit.ApiService
import com.example.storyapp.utils.SettingsPreferences
import com.example.storyapp.utils.SharedPreference
import com.example.storyapp.utils.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val usersDao: UsersDao,
    private val pref: SettingsPreferences
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _detailData = MutableLiveData<Story>()
    val detailData: LiveData<Story> = _detailData

    suspend fun getToken(): String {
        return pref.getToken().first()
    }

    suspend fun registerUser(users: UsersEntity) {
        _isLoading.value = true
        val requestName = users.name
        val requestEmail = users.email
        val requestPassword = users.password

        try {
            apiService.register(requestName, requestEmail, requestPassword)

            GlobalScope.launch(Dispatchers.IO) {
                usersDao.insertUsers(users)
            }

            _isLoading.value = false

        } catch (e: Exception) {
            _isLoading.value = false
        }
    }

    suspend fun setLoginUser(email: String, password: String): Boolean {
        _isLoading.value = true
        val requestEmail = email
        val requestPassword = password

        try {
            val response = apiService.login(requestEmail, requestPassword)

            if (!response.error) {
                _isLoading.value = false
                pref.saveToken(response.loginResult.token)
                Log.d(TAG, "Login is Successfull : TOKEN : ${response.loginResult.token}}")
                return true
            } else {
                _isLoading.value = false
                return false
            }

        } catch (e: Exception) {
            _isLoading.value = false
            return false
        }
    }

    suspend fun getListStory() {
        _isLoading.value = true

        try {
            val response = apiService.getStories()
            Log.d(TAG, "List Story : $response")

            if (!response.error) {
                _isLoading.value = false
                Log.d(TAG, "List Story : $response")
                _listStory.value = response.listStory.map { it!! }
            } else {
                Log.d(TAG, "getListStory: gagal")
                _isLoading.value = false
            }

        } catch (e: Exception) {
            _isLoading.value = false
        }
    }



    suspend fun getDetailStory(id: String) {
        _isLoading.value = true
        try {
            val detailStory = apiService.getDetailStories(id)
            _isLoading.value = false
            _detailData.value = detailStory.story
        } catch (e: Exception) {
            _isLoading.value = false
        }
    }

    suspend fun insertStories(file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        try {
            _isLoading.value = false
            apiService.insertStories(file, description)
        } catch (e: HttpException) {
            _isLoading.value = false
            Log.d(TAG, "Error : ${e.message().toString()}")
        }
    }

    suspend fun logout() {
        pref.deleteToken()
    }

    suspend fun saveUser(token: String) {
        pref.saveToken(token)
    }


    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            usersDao: UsersDao,
            pref: SettingsPreferences
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, usersDao, pref)
        }.also { instance = it }
    }


}