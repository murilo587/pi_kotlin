package com.example.tagarela.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.models.Card
import com.example.tagarela.data.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchAllCards()
    }

    private fun fetchAllCards() {
        _loading.value = true
        viewModelScope.launch {
            try {
                _cards.value = repository.getAllCards()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar"
            } finally {
                _loading.value = false
            }
        }
    }
}
