package com.example.tagarela.data.models

data class UserResponse(
    val message: String,
    val user: User
)

data class User(
    val email: String,
    val username: String,
    val password: String
)