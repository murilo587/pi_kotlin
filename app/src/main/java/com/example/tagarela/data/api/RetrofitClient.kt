package com.example.tagarela.data.api

import android.content.Context
import com.example.tagarela.utils.AuthInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/v1/"

    fun createApiService(context: Context): ApiService {

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(MultipartInterceptor())
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

class MultipartInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val isMultipartRequest = request.headers["Content-Type"]?.contains("multipart/form-data") == true
        println("🔍 Headers antes de enviar a requisição:")
        request.headers.forEach { header ->
            println("${header.first}: ${header.second}")
        }
        return if (isMultipartRequest) {
            val newRequest = request.newBuilder()
                .addHeader("Content-Type", "multipart/form-data")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}

