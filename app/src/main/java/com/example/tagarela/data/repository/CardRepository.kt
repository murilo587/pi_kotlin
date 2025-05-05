package com.example.tagarela.data.repository

import android.content.Context
import com.example.tagarela.data.api.RetrofitClient
import com.example.tagarela.data.models.Card

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
}
