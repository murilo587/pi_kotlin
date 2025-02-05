package com.example.tagarela.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.tagarela.data.models.*
import com.example.tagarela.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun login(email: String, password: String): Result<Any?> {
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

    suspend fun registerUser(request: SignUpRequest): Result<Any?> {
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

    suspend fun updateUser(userId: String, username: String, email: String, password: String): Result<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val user = User(email = email, username = username, password = password)
                val response: Response<UserResponse> = RetrofitClient.apiService.updateUser(userId, user)
                if (response.isSuccessful) {
                    Result(success = true, message = "User updated successfully")
                } else {
                    Result(success = false, error = "Error updating user data")
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Error updating user data"
                Result(success = false, error = errorMessage)
            }
        }
    }
}

data class Result<T>(val success: Boolean, val message: String? = null, val userId: String? = null, val error: String? = null)
data class UserResult(val success: Boolean, val user: User? = null, val error: String? = null)
