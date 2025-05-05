package com.example.tagarela.data.api

import GameResponse
import com.example.tagarela.data.models.Card
import com.example.tagarela.data.models.NewCard
import com.example.tagarela.data.models.SignInRequest
import com.example.tagarela.data.models.SignInResponse
import com.example.tagarela.data.models.SignUpRequest
import com.example.tagarela.data.models.SignUpResponse
import com.example.tagarela.data.models.User
import com.example.tagarela.data.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("user/login")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse
    @POST("register")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse
    @GET("item")
    suspend fun getAllItems(): List<Card>
    @GET("item/{id}")
    suspend fun getCardById(@Path("id") cardId: String): Card
    @Multipart
        @POST("item/user/{userId}")
        suspend fun addNewCard(
            @Path("userId") userId: String,
            @Part("name") name: RequestBody,
            @Part("syllables") syllables: RequestBody,
            @Part("category") category: RequestBody,
            @Part("subcategory") subcategory: RequestBody,
            @Part image: MultipartBody.Part,
            @Part video: MultipartBody.Part,
            @Part audio: MultipartBody.Part
        ): Response<NewCard>
    @GET("item/recents")
    suspend fun getRecentCards(): List<Card>
    @GET("item/history")
    suspend fun getMostUsedCards(): List<Card>
    @GET("user/{id}")
    fun getUser(@Path("id") userId: String): Call<UserResponse>
    @GET("quiz/random")
    suspend fun fetchAllQuizzes(): GameResponse
    @PUT("user/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<UserResponse>
}