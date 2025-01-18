package com.example.tagarela.data.api

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val userId: String, val message: String, val error: String?)

interface ApiService {
    @POST("logar")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}