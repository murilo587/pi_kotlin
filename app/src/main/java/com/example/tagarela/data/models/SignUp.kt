package com.example.tagarela.data.models

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val username: String,
    val password: String
)

data class SignUpResponse(
    val accessToken: String,
    val tokenType: String,
    val id: String,
    val username: String
)