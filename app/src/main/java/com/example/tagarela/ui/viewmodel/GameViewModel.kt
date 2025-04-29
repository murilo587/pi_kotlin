package com.example.tagarela.ui.viewmodel

import Game
import GameResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _gameResponse = MutableStateFlow<GameResponse?>(null)
    val gameResponse: StateFlow<GameResponse?> get() = _gameResponse

    private val _currentGameIndex = MutableStateFlow(0)
    val currentGameIndex: StateFlow<Int> get() = _currentGameIndex

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> get() = _isQuizFinished

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> get() = _loading

    val games: List<Game> get() = _gameResponse.value?.games ?: emptyList()

    init {
        loadGameData()
    }

    fun loadGameData() {
        viewModelScope.launch {
            _loading.value = true
            val gameData = repository.getGameData()
            _gameResponse.value = gameData
            _loading.value = false
        }
    }

    fun nextGame() {
        if (_currentGameIndex.value < games.size - 1) {
            _currentGameIndex.value += 1
        } else {
            _isQuizFinished.value = true
        }
    }
}
