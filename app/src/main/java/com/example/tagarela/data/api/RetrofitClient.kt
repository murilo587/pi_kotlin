    package com.example.tagarela.data.api

    import android.content.Context
    import com.example.tagarela.utils.AuthInterceptor
    import com.example.tagarela.data.UserPreferences
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.runBlocking
    import okhttp3.OkHttpClient
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory

    object RetrofitClient {
        private const val BASE_URL = "http://10.0.2.2:8080/v1/"

        fun createApiService(context: Context): ApiService {
            val userPreferences = UserPreferences(context)
            val token = runBlocking {
                userPreferences.accessToken.first() ?: ""
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        fun createUnauthenticatedApiService(): ApiService {
            val clientWithoutAuth = OkHttpClient.Builder().build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientWithoutAuth)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

    }
