package com.example.storyapp.ui.login

import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.model.SessionModel
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.SettingsPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(
    private val usersRepository: UserRepository,
) : ViewModel() {

    private val _account = MutableLiveData<ResultState<LoginResponse>>()
    val account : LiveData<ResultState<LoginResponse>> = _account

    val getListStory: LiveData<PagingData<ListStoryItem>> = usersRepository.getStories().cachedIn(viewModelScope)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            usersRepository.setLoginUser(email, password).asFlow().collect{
                _account.value = it
            }
        }
    }

    fun setupAction(sessionModel: SessionModel){
        viewModelScope.launch {
            usersRepository.saveSession(sessionModel)
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return usersRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            usersRepository.logout()
        }
    }

    fun getStory(token: String) = usersRepository.getListStory(token)

}