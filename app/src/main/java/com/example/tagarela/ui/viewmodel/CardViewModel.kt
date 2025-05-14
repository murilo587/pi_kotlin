package com.example.tagarela.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tagarela.data.models.Card
import com.example.tagarela.data.models.NewCard
import com.example.tagarela.data.models.UserCard
import com.example.tagarela.data.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class CardViewModel(private val repository: CardRepository) : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    private val _userCards = MutableStateFlow<List<UserCard>>(emptyList())
    val userCards: StateFlow<List<UserCard>> = _userCards

    private val _recentCards = MutableStateFlow<List<Card>>(emptyList())
    val recentCards: StateFlow<List<Card>> = _recentCards

    private val _mostUsedCards = MutableStateFlow<List<Card>>(emptyList())
    val mostUsedCards: StateFlow<List<Card>> = _mostUsedCards

    private val _selectedCard = MutableStateFlow<Card?>(null)
    val selectedCard: StateFlow<Card?> = _selectedCard

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _category = MutableStateFlow("meus cartoes")
    val category: StateFlow<String> = _category

    private val _addCardResponse = MutableStateFlow<Response<NewCard>?>(null)
    val addCardResponse: StateFlow<Response<NewCard>?> = _addCardResponse

    init {
        Log.d("CardViewModel", "ViewModel foi inicializado.")
        fetchAllCards()
        fetchRecentCards()
        fetchMostUsedCards()
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

    fun fetchMostUsedCards() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val mostUsedCardsResponse = repository.getMostUsedCards()
                _mostUsedCards.value = mostUsedCardsResponse
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar os cartões mais usados"
            }
            _loading.value = false
        }
    }

    fun fetchUserCards(userId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val userCardsResponse = repository.getUserCards(userId)
                _userCards.value = userCardsResponse
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar os cartões do usuário"
                Log.e("CardViewModel", "Erro ao buscar cartões: ${e.message}")
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
                _error.value = e.message ?: "Erro ao buscar detalhes do cartão"
            }
            _loading.value = false
        }
    }

    fun uploadImageAndGetCategory(imageFile: File) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

                val response = repository.uploadImage(imagePart)

                if (response.isSuccessful) {
                    _category.value = response.body()?.category ?: "default"
                } else {
                    _category.value = "default"
                }
            } catch (e: Exception) {
                _category.value = "default"
            }
            _loading.value = false
        }
    }


    fun addNewCard(newCard: NewCard, userId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val name = createPartFromString(newCard.name)
                val syllables = createPartFromString(newCard.syllables)
                val category = createPartFromString(newCard.category)
                val subcategory = createPartFromString(newCard.subcategory)

                val imagePart = createPartFromFile(newCard.image, "image/jpeg", "image")
                val videoPart = createPartFromFile(newCard.video, "video/mp4", "video")
                val audioPart = createPartFromFile(newCard.audio, "audio/mp3", "audio")

                val response = repository.addNewCard(
                    userId, name, syllables, category, subcategory,
                    imagePart, videoPart, audioPart
                )
                println("VM: ${response}")
                _addCardResponse.value = response
                _error.value = if (response.isSuccessful) null else "Erro ao adicionar o novo cartão"
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro inesperado"
            }
            _loading.value = false
        }
    }

    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun createPartFromFile(file: File, mimeType: String, fieldName: String): MultipartBody.Part {
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
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
