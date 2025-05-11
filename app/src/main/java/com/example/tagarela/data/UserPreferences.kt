package com.example.tagarela.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(context: Context) {

    private val dataStore = context.dataStore
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val XSRF_TOKEN_KEY = stringPreferencesKey("xsrf_token")

    val userId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    val accessToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_ACCESS_TOKEN]
        }

    val xsrfToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[XSRF_TOKEN_KEY]
        }

    val userName: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_NAME]
        }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[USER_ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveXsrfToken(xsrfToken: String) {
        dataStore.edit { preferences ->
            preferences[XSRF_TOKEN_KEY] = xsrfToken
        }
    }

    suspend fun saveUserName(userName: String) {
        println("novo name:" + userName)
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

}
