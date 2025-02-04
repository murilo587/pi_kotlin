package com.example.tagarela.data.models

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)

data class SignUpResponse(
    val message: String,
    @SerializedName("user_id") val userId: String
)