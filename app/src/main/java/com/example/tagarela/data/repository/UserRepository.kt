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

    suspend fun login(username: String, password: String): Result<Any?> {
        return withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            try {
                val response = RetrofitClient.apiService.signIn(SignInRequest(username, password))
                editor.apply()
                Result(success = true, message = "Login realizado com sucesso", userId = response.accessToken)
            } catch (e: Exception) {
                Result(success = false, error = "Falha no login")
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
                Result(success = true, message = "Cadastro realizado com sucesso", userId = response.userId)
            } catch (e: Exception) {
                Result(success = false, error = "Falha no cadastro")
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
                    } ?: UserResult(success = false, error = "Usuário não encontrado")
                } else {
                    UserResult(success = false, error = "Falha ao puxar dados do usuário")
                }
            } catch (e: Exception) {
                UserResult(success = false, error = "Falha ao puxar dados do usuário")
            }
        }
    }

    suspend fun updateUser(userId: String, username: String, email: String, password: String): Result<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val user = User(email = email, username = username, password = password)
                val response: Response<UserResponse> = RetrofitClient.apiService.updateUser(userId, user)
                if (response.isSuccessful) {
                    Result(success = true, message = "Usuário atualizado com sucesso")
                } else {
                    Result(success = false, error = "Falha ao atualizar dados do usuário")
                }
            } catch (e: Exception) {
                Result(success = false, error = "Falha ao atualizar dados do usuário")
            }
        }
    }
}

data class Result<T>(val success: Boolean, val message: String? = null, val userId: String? = null, val error: String? = null)
data class UserResult(val success: Boolean, val user: User? = null, val error: String? = null)
