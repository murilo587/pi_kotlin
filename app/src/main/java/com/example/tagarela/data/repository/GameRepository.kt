package com.example.tagarela.data.repository

import android.content.Context
import com.example.tagarela.data.api.RetrofitClient
import com.example.tagarela.data.models.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class GameResult<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
)

class GameRepository(private val context: Context) {

    private val apiService = RetrofitClient.createApiService(context)

    suspend fun fetchQuizData(level: Int): GameResult<List<Question>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getQuizData(level)

                return@withContext if (response.isNotEmpty()) {
                    saveLastAccessedLevel(level)
                    GameResult(
                        success = true,
                        message = "Dados carregados com sucesso",
                        data = response
                    )
                } else {
                    GameResult(
                        success = false,
                        error = "Nenhum dado disponível para o nível $level"
                    )
                }
            } catch (e: Exception) {
                GameResult(
                    success = false,
                    error = e.message ?: "Erro ao carregar os dados"
                )
            }
        }
    }

    private fun saveLastAccessedLevel(level: Int) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("last_accessed_level", level)
        editor.apply()
    }
}
