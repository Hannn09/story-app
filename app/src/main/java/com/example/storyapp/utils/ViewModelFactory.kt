package com.example.storyapp.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.UserRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.register.RegisterViewModel
import com.example.storyapp.ui.story.StoryViewModel

class ViewModelFactory(private val userRepository: UserRepository, private val preferences: SettingsPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository, preferences) as T
        } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), SettingsPreferences(context.dataStore))
            }.also { instance = it }
    }
}