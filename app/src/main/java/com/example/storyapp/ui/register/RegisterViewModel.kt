package com.example.storyapp.ui.register


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val usersRepository: UserRepository): ViewModel() {

    private val _registrationResult = MutableLiveData<Pair<Boolean, String>>()
    val registrationResult: LiveData<Pair<Boolean, String>> = _registrationResult


    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                usersRepository.registerUser(name, email, password)
                _registrationResult.value = Pair(true, "Registration Successful!")
            } catch (e: Exception) {
                _registrationResult.value = Pair(false, "Registration Failed!")
            }

        }
    }
}