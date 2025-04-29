package com.example.tagarela.data.repository

import GameResponse
import android.content.Context
import com.example.tagarela.data.api.RetrofitClient

class GameRepository(private val context: Context) {

    private val apiService = RetrofitClient.createApiService(context)

    suspend fun getGameData(): GameResponse {
        return apiService.fetchAllQuizzes()
    }
}
