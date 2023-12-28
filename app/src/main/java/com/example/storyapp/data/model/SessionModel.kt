package com.example.storyapp.data.model

data class SessionModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
