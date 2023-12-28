package com.example.storyapp.utils


import android.content.Context
import android.content.SharedPreferences
import android.se.omapi.Session
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.model.SessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun saveSession(user: SessionModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = user.email
            preferences[TOKEN] = user.token
            preferences[IS_LOGIN] = true
        }
    }

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { preferences ->
            SessionModel(
                preferences[EMAIL] ?: "",
                preferences[TOKEN] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null
        private val TOKEN = stringPreferencesKey("token")
        private val EMAIL = stringPreferencesKey("email")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")

        fun getInstance(
            dataStore: DataStore<Preferences>,
        ): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}