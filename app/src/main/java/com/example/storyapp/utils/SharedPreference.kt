package com.example.storyapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

class SharedPreference(activity: Context) {
    val login = "login"
    val token = "token"
    val myPref = "Main_Pref"
    val sharedPreference: SharedPreferences

    init {
        sharedPreference = activity.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean) {
        sharedPreference.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sharedPreference.getBoolean(login, false)
    }

    fun setTokenLogin(username: String) {
        sharedPreference.edit().putString(token, username).apply()
    }

    fun geTokenLogin(): String? {
        return sharedPreference.getString(token, "")
    }

    fun clearLoginData() {
        sharedPreference.edit().putString(token, "").apply()
        sharedPreference.edit().putBoolean(login, false).apply()
    }
}