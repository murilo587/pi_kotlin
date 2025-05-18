package com.example.tagarela.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.repository.Result
import com.example.tagarela.data.repository.UserRepository
import com.example.tagarela.data.UserPreferences
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SignInViewModel(
    private val repository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var loginResult = mutableStateOf<Result<Any?>?>(null)
        private set

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _state = MutableStateFlow<String?>(null)
    val state: StateFlow<String?> = _state

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            loginResult.value = null
            _state.value = null
            try {
                val result = repository.login(username, password)
                loginResult.value = result
                if (result.success) {
                    result.username?.let { userPreferences.saveUserName(it) }
                    result.userId?.let { userPreferences.saveUserId(it) }
                    result.accessToken?.let { userPreferences.saveAccessToken(it) }
                    _state.value = result.message
                } else {
                    _state.value = result.error
                }
            } catch (e: Exception) {
                _state.value = "Erro desconhecido ao fazer login"
            } finally {
                _loading.value = false
            }
        }
    }
}
