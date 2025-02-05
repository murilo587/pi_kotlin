package com.example.tagarela.data.api

import com.example.tagarela.data.models.Card
import com.example.tagarela.data.models.CardResponse
import com.example.tagarela.data.models.SignInRequest
import com.example.tagarela.data.models.SignInResponse
import com.example.tagarela.data.models.SignUpRequest
import com.example.tagarela.data.models.SignUpResponse
import com.example.tagarela.data.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("logar")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse
    @POST("register")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse
    @GET("items")
    suspend fun getAllItems(): CardResponse
    @GET("user/{id}")
    fun getUser(@Path("id") userId: String): Call<UserResponse>
}