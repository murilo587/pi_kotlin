package com.example.tagarela.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.data.models.*
import com.example.tagarela.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val unauthenticatedApiService = RetrofitClient.createUnauthenticatedApiService()
    private val authenticatedApiService = RetrofitClient.createApiService(context)

    private val userPreferences = UserPreferences(context)

    suspend fun login(username: String, password: String): Result<Any?> {
        return withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            try {
                val response = unauthenticatedApiService.signIn(SignInRequest(username, password))

                if (response.isSuccessful) {

                    val xsrfToken = response.headers()["X-XSRF-TOKEN"]
                    xsrfToken?.let {
                        editor.putString("xsrf_token", xsrfToken)
                        editor.apply()
                        userPreferences.saveXsrfToken(xsrfToken)
                        println("CSRF Token capturado: $xsrfToken")
                    }

                    val body = response.body()
                    if (body != null) {
                        userPreferences.setLoggedStatus(true)
                        editor.putString("user_name", body.username)
                        editor.putString("access_token", body.accessToken)
                        editor.putString("user_id", body.id.toString())
                        editor.apply()

                        userPreferences.saveUserName(body.username)
                        userPreferences.saveAccessToken(body.accessToken)
                        userPreferences.saveUserId(body.id.toString())


                        return@withContext Result(
                            success = true,
                            message = "Login realizado com sucesso",
                            userId = body.id,
                            accessToken = body.accessToken
                        )
                    } else {
                        return@withContext Result(
                            success = false,
                            error = "Resposta vazia do servidor"
                        )
                    }
                } else {
                    return@withContext Result(
                        success = false,
                        error = "Falha no login: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                return@withContext Result(success = false, error = "Falha no login: ${e.message}")
            }
        }
    }

    suspend fun registerUser(request: SignUpRequest): Result<Any?> {
        return withContext(Dispatchers.IO) {

            try {
                val response = unauthenticatedApiService.signUp(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val editor = sharedPreferences.edit()
                        userPreferences.setLoggedStatus(true)
                        editor.putString("user_name", body.username)
                        editor.putString("access_token", body.accessToken)
                        editor.putString("user_id", body.id)
                        editor.apply()

                        userPreferences.saveUserName(body.username)
                        userPreferences.saveAccessToken(body.accessToken)
                        println("Token jwt salvo ${body.accessToken}")
                        userPreferences.saveUserId(body.id)

                        Result(
                            success = true,
                            message = "Cadastro realizado com sucesso",
                            userId = body.id
                        )
                    } else {
                        Result(success = false, error = "Resposta da API vazia")
                    }
                } else {
                    Result(success = false, error = "Erro")
                }
            } catch (e: Exception) {
                Result(success = false, error = "Falha no cadastro")
            }
        }
    }

    suspend fun updateUser(userId: String, username: String, password: String): Result<Any?> {
        return withContext(Dispatchers.IO) {
            try {
                val user = User(username = username, password = password)
                val response: Response<UserResponse> =
                    authenticatedApiService.updateUser(userId, user)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        userPreferences.saveAccessToken(body.accessToken)
                        return@withContext Result(
                            success = true,
                            message = "Usuário atualizado com sucesso",
                            username = body.username
                        )
                    } else {
                        return@withContext Result(
                            success = false,
                            error = "Erro: Corpo da resposta está vazio"
                        )
                    }
                } else {
                    return@withContext Result(
                        success = false,
                        error = "Erro ao atualizar usuário: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                return@withContext Result(
                    success = false,
                    error = "Falha ao atualizar dados do usuário: ${e.message}"
                )
            }
        }
    }
}

data class Result<T>(val success: Boolean, val message: String? = null, val userId: String? = null, val error: String? = null, val accessToken: String? = null, val username: String? = null)
data class UserResult(val success: Boolean, val user: User? = null, val error: String? = null)
