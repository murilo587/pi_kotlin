package com.example.tagarela.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.repository.Result
import com.example.tagarela.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    var loginResult = mutableStateOf<Result?>(null)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            loginResult.value = result
        }
    }
}
