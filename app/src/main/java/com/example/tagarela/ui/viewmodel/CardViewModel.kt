package com.example.tagarela.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.models.Card
import com.example.tagarela.data.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    private val _recentCards = MutableStateFlow<List<Card>>(emptyList())
    val recentCards: StateFlow<List<Card>> = _recentCards

    private val _selectedCard = MutableStateFlow<Card?>(null)
    val selectedCard: StateFlow<Card?> = _selectedCard

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchAllCards()
        fetchRecentCards()
    }

    fun fetchAllCards() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val cardsResponse = repository.getAllCards()
                _cards.value = cardsResponse
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar os cartões"
            }
            _loading.value = false
        }
    }

    fun fetchRecentCards() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val recentCardsResponse = repository.getRecentCards()
                _recentCards.value = recentCardsResponse
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar os cartões recentes"
            }
            _loading.value = false
        }
    }

    fun fetchCardDetails(cardId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val cardDetails = repository.getCardById(cardId)
                _selectedCard.value = cardDetails
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao buscar detalhes do card"
            }
            _loading.value = false
        }
    }

    class Factory(private val repository: CardRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
