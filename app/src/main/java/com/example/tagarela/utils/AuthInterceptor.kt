package com.example.tagarela.utils

import android.content.Context
import com.example.tagarela.data.UserPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val token = runBlocking {
            UserPreferences(context).accessToken.first() ?: ""
        }

        val xsrfToken = runBlocking {
            UserPreferences(context).xsrfToken.first() ?: ""
        }
        println("CSRF Token carregado: $xsrfToken")

        val newRequestBuilder = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
//
//        if (originalRequest.method == "POST" || originalRequest.method == "PUT") {
//            newRequestBuilder.addHeader("X-XSRF-TOKEN", xsrfToken)
//        }

        val newRequest = newRequestBuilder.build()

        return chain.proceed(newRequest)
    }
}
