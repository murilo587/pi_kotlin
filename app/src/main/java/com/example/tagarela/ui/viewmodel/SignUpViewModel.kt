package com.example.tagarela.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.repository.Result
import com.example.tagarela.data.repository.UserRepository
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.data.models.SignUpRequest
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository, private val userPreferences: UserPreferences) : ViewModel() {
    var signUpResult = mutableStateOf<Result?>(null)
        private set

    fun signUp(username: String, email: String, password: String, repeatPassword: String) {
        if (password != repeatPassword) {
            signUpResult.value = Result(success = false, error = "As senhas não coincidem")
            return
        }

        viewModelScope.launch {
            val request = SignUpRequest(username, email, password)
            val result = repository.registerUser(request)
            signUpResult.value = result
            if (result.success) {
                result.userId?.let { userPreferences.saveUserId(it) }
            }
        }
    }
}
