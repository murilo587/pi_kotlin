package com.example.tagarela.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.models.Question
import com.example.tagarela.data.repository.GameRepository
import com.example.tagarela.data.repository.GameResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _gameState = MutableStateFlow<GameResult<List<Question>>>(GameResult(success = false, data = emptyList()))
    val gameState: StateFlow<GameResult<List<Question>>> get() = _gameState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> get() = _currentQuestionIndex

    fun loadGameData(level: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = repository.fetchQuizData(level)
                _gameState.value = result
                if (result.success && !result.data.isNullOrEmpty()) {
                    _currentQuestionIndex.value = 0
                }
            } catch (e: Exception) {
                _gameState.value = GameResult(success = false, error = e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    fun onAnswerSelected(selectedIndex: Int) {
        val currentQuestion = _gameState.value.data?.getOrNull(_currentQuestionIndex.value)
        if (currentQuestion?.correctAnswerIndex == selectedIndex) {
            if (_currentQuestionIndex.value < (_gameState.value.data?.size ?: 0) - 1) {
                _currentQuestionIndex.value++
            }
        }
    }
}
