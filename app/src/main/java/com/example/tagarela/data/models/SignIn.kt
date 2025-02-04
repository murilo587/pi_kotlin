package com.example.tagarela.data.models

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    val email: String,
    val password: String
)
data class SignInResponse(
    val message: String,
    @SerializedName("user_id") val userId: String
)