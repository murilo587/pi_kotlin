package com.example.tagarela.data.api

import com.example.tagarela.data.models.Card
import com.example.tagarela.data.models.CardResponse
import com.example.tagarela.data.models.SignInRequest
import com.example.tagarela.data.models.SignInResponse
import com.example.tagarela.data.models.SignUpRequest
import com.example.tagarela.data.models.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("logar")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse
    @POST("register")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse
    @GET("items")
    suspend fun getAllItems(): CardResponse
}