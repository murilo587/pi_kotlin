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
        println("Buscando card ID na API: $cardId") // 🔹 Log para verificar se o ID está correto
        return apiService.getCardById(cardId)
    }
}
