package com.example.tagarela.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.repository.Result
import com.example.tagarela.data.repository.UserRepository
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.data.models.SignUpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository, private val userPreferences: UserPreferences) : ViewModel() {
    var signUpResult = mutableStateOf<Result<Any?>?>(null)
        private set

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _state = MutableStateFlow<String?>(null)
    val state: StateFlow<String?> = _state

    fun signUp(username: String, password: String, repeatPassword: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                if (username.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
                    _state.value = "Por favor, preencha todos os campos."
                    _loading.value = false
                    return@launch
                }

                if (password != repeatPassword) {
                    _state.value = "Senhas não coincidem."
                    _loading.value = false
                    return@launch
                }

                val request = SignUpRequest(username, password)
                val result = repository.registerUser(request)

                signUpResult.value = result
                if (result.success) {
                    result.accessToken?.let { userPreferences.saveAccessToken(it) }
                    result.username?.let { userPreferences.saveUserName(it) }
                    result.userId?.let { userPreferences.saveUserId(it) }

                    _state.value = result.message
                } else {
                    _state.value = result.error ?: "Erro desconhecido."
                }

                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _state.value = "Erro ao tentar cadastrar: ${e.message}"
            }
        }
    }
}
