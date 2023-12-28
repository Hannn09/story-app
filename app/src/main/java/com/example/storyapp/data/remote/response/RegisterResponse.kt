package com.example.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

