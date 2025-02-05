package com.example.tagarela.ui.viewmodel

import android.util.Log
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.models.User
import com.example.tagarela.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.tagarela.data.repository.Result // Importando a classe corrigida

class AccountViewModel(context: Context) : ViewModel() {
    private val userRepository = UserRepository(context)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _updateResult = MutableStateFlow<Result<Any?>?>(null) // Usando Result
    val updateResult: StateFlow<Result<Any?>?> = _updateResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getUserData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userResult = userRepository.getUserData(userId)
                _isLoading.value = false
                if (userResult.success) {
                    _user.value = userResult.user
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = userResult.error ?: "Erro ao obter dados do usuário"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Erro desconhecido ao obter dados do usuário"
            }
        }
    }

    fun updateUser(userId: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = userRepository.updateUser(userId, username, email, password)
                _isLoading.value = false
                _updateResult.value = result
                if (!result.success) {
                    _errorMessage.value = result.error ?: "Erro ao atualizar dados do usuário"
                } else {
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Erro desconhecido ao atualizar dados do usuário"
            }
        }
    }
}
