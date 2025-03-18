package com.example.tagarela.data.repository

import com.example.tagarela.data.api.RetrofitClient
import com.example.tagarela.data.models.Card

class CardRepository {
    suspend fun getAllCards(): List<Card> {
        return RetrofitClient.apiService.getAllItems()
    }
}
