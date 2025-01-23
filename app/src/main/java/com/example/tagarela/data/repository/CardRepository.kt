package com.example.tagarela.data.repository

import com.example.tagarela.data.api.RetrofitClient
import com.example.tagarela.data.models.Card

class CardRepository {
    suspend fun getAllCards(): List<Card> {
        val response = RetrofitClient.apiService.getAllItems()
        return response.items
    }
}
