package com.example.tagarela.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)
data class LoginResponse(
    val message: String,
    @SerializedName("user_id") val userId: String
)