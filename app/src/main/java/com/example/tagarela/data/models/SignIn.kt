package com.example.tagarela.data.models

data class SignInRequest(
    val username: String,
    val password: String
)
data class SignInResponse(
    val accessToken: String,
    val tokenType: String,
    val id: String,
    val username: String
)