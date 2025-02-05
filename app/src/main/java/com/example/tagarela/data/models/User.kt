package com.example.tagarela.data.models

data class UserResponse(
    val message: String,
    val user: User
)

data class User(
    val email: String,
    val my_items: List<String>,
    val username: String
)