package com.example.tagarela.data.repository

import ImageCategoryResponse
import android.content.Context
import com.example.tagarela.data.api.RetrofitClient
import com.example.tagarela.data.models.Card
import com.example.tagarela.data.models.NewCard
import com.example.tagarela.data.models.UserCard
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class CardRepository(private val context: Context) {

    private val apiService = RetrofitClient.createApiService(context)

    suspend fun getAllCards(): List<Card> {
        return apiService.getAllItems()
    }

    suspend fun getCardById(cardId: String): Card {
        return apiService.getCardById(cardId)
    }

    suspend fun getRecentCards(): List<Card> {
        return apiService.getRecentCards()
    }

    suspend fun getMostUsedCards(): List<Card> {
        return apiService.getMostUsedCards()
    }

    suspend fun getUserCards(userId: String): List<UserCard> {
        val response = apiService.getUserCards(userId)

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Erro ao buscar cartões")
        }
    }

    suspend fun uploadImage(imagePart: MultipartBody.Part): Response<ImageCategoryResponse> {
        return try {
            apiService.uploadImage(imagePart)
        } catch (e: Exception) {
            Response.success(null)
        }
    }

    suspend fun addNewCard(
        userId: String,
        name: RequestBody,
        syllables: RequestBody,
        category: RequestBody,
        subcategory: RequestBody,
        image: MultipartBody.Part,
        video: MultipartBody.Part,
        audio: MultipartBody.Part
    ): Response<NewCard> {
        return apiService.addNewCard(
            userId,
            name,
            syllables,
            category,
            subcategory,
            image,
            video,
            audio
        )
    }

    }
