package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.utils.SettingsPreferences
import com.example.storyapp.utils.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = SettingsPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}