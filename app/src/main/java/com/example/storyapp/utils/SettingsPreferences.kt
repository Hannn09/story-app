package com.example.storyapp.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="settings")

class SettingsPreferences  constructor(private val dataStore: DataStore<Preferences>) {


    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: "Error"
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }


    suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferences? = null
        private val TOKEN = stringPreferencesKey("token")
        fun getInstance(
            dataStore: DataStore<Preferences>
        ): SettingsPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}