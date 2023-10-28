package com.example.storyapp.di

import android.content.Context
import android.util.Log
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.local.room.UsersDatabase
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.data.remote.retrofit.ApiService
import com.example.storyapp.utils.SettingsPreferences
import com.example.storyapp.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = SettingsPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first()  }
        val apiService = ApiConfig.getApiService(user)
        val database = UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        return UserRepository.getInstance(apiService, dao, pref)
    }

//    fun provideRepository(context: Context): UserRepository = runBlocking {
//        val pref = SettingsPreferences.getInstance(context.dataStore)
//        val user = pref.getToken().first()
//        val apiService = ApiConfig.getApiService(user)
//        val database = UsersDatabase.getInstance(context)
//        val dao = database.usersDao()
//        UserRepository.getInstance(apiService, dao, pref)
//    }
}