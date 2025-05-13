package com.example.tagarela.ui.viewmodel

import android.util.Log
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tagarela.data.models.User
import com.example.tagarela.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.tagarela.data.repository.Result
import com.example.tagarela.data.UserPreferences
import kotlinx.coroutines.flow.firstOrNull

class AccountViewModel(
    private val context: Context,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val userRepository = UserRepository(context)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _updateResult = MutableStateFlow<Result<Any?>?>(null)
    val updateResult: StateFlow<Result<Any?>?> = _updateResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun updateUser(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = userPreferences.userId.firstOrNull()

                if (userId.isNullOrBlank()) {
                    _errorMessage.value = "Erro: ID do usuário não encontrado"
                    _isLoading.value = false
                    return@launch
                }

                val result = userRepository.updateUser(userId, username, password)

                if (result.success) {
                    result.username?.let {
                        Log.d("AccountViewModel", "Salvando novo username: $it")
                        userPreferences.saveUserName(it)
                    }
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = result.error ?: "Erro ao atualizar dados do usuário"
                }

                _updateResult.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro desconhecido ao atualizar dados do usuário"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(navHostController: NavHostController) {
        viewModelScope.launch {
            userPreferences.setLoggedStatus(false)
            userPreferences.clearAll()
            navHostController.navigate("signin") {
                popUpTo("myaccount") { inclusive = true }
            }
        }
    }
}

class AccountViewModelFactory(
    private val context: Context,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(context, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
