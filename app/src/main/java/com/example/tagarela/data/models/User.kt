package com.example.tagarela.data.models

data class UserResponse(
    val accessToken: String,
    val tokenType: String,
    val id: String,
    val username: String
)

data class User(
    val username: String,
    val password: String
)