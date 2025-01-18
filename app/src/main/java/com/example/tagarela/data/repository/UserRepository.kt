package com.example.tagarela.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.tagarela.data.api.LoginRequest
import com.example.tagarela.data.api.LoginResponse
import com.example.tagarela.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun login(email: String, password: String): Result {
        return withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, password))
                editor.putString("user_id", response.userId)
                editor.apply()
                Result(success = true, message = response.message, userId = response.userId)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Login Error"
                Result(success = false, error = errorMessage)
            }
        }
    }
}

data class Result(val success: Boolean, val message: String? = null, val userId: String? = null, val error: String? = null)
