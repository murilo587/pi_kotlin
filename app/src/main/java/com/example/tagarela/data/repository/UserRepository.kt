package com.example.tagarela.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.tagarela.data.models.SignInRequest
import com.example.tagarela.data.models.SignUpRequest
import com.example.tagarela.data.models.User
import com.example.tagarela.data.models.UserResponse
import com.example.tagarela.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun login(email: String, password: String): Result {
        return withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            try {
                val response = RetrofitClient.apiService.signIn(SignInRequest(email, password))
                editor.putString("user_id", response.userId)
                editor.apply()
                Result(success = true, message = response.message, userId = response.userId)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Login Error"
                Result(success = false, error = errorMessage)
            }
        }
    }

    suspend fun registerUser(request: SignUpRequest): Result {
        return withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            try {
                val response = RetrofitClient.apiService.signUp(request)
                editor.putString("user_id", response.userId)
                editor.apply()
                Result(success = true, message = response.message, userId = response.userId)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Registration Error"
                Result(success = false, error = errorMessage)
            }
        }
    }

    suspend fun getUserData(userId: String): UserResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getUser(userId).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        UserResult(success = true, user = it.user)
                    } ?: UserResult(success = false, error = "User not found")
                } else {
                    UserResult(success = false, error = "Error fetching user data")
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Error fetching user data"
                UserResult(success = false, error = errorMessage)
            }
        }
    }
}

data class Result(val success: Boolean, val message: String? = null, val userId: String? = null, val error: String? = null)
data class UserResult(val success: Boolean, val user: User? = null, val error: String? = null)
