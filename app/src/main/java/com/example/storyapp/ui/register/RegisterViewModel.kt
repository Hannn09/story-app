package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.local.entity.UsersEntity
import kotlinx.coroutines.launch

class RegisterViewModel(private val usersRepository: UserRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = usersRepository.isLoading

    private val _registrationResult = MutableLiveData<Pair<Boolean, String>>()
    val registrationResult: LiveData<Pair<Boolean, String>> = _registrationResult


    fun register(users: UsersEntity) {
        viewModelScope.launch {
            try {
                usersRepository.registerUser(users)
                _registrationResult.value = Pair(true, "Registration Successful!")
            } catch (e: Exception) {
                _registrationResult.value = Pair(false, "Registration Failed!")
            }
        }
    }
}