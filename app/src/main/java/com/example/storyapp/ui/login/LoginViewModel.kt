package com.example.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.utils.SettingsPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val usersRepository: UserRepository, private val pref: SettingsPreferences): ViewModel() {

    val isLoading: LiveData<Boolean> = usersRepository.isLoading

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    fun getSaveToken(): LiveData<String> = pref.getToken().asLiveData()


    fun setIsLogin(isLoggedIn: Boolean) {
        _isLogin.postValue(isLoggedIn)
    }

     fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val success =  usersRepository.setLoginUser(email, password)
                if (success) {
                    Log.d("LoginViewModel", "Login successful")
                    setIsLogin(true)
                } else {
                    setIsLogin(false)
                }
            } catch (e: Exception) {
                setIsLogin(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            usersRepository.logout()
        }
    }
}