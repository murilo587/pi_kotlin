package com.example.tagarela.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.data.models.Card
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.components.CardView
import com.example.tagarela.ui.components.CustomModal
import com.example.tagarela.ui.viewmodel.CardViewModel
import com.example.tagarela.R

class CardViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel: CardViewModel = viewModel(factory = CardViewModelFactory(CardRepository()))

    val textState = remember { mutableStateOf("") }
    val filteredCards = remember { mutableStateOf(listOf<Card>()) }
    val modalVisible = remember { mutableStateOf(false) }
    val selectedCard = remember { mutableStateOf<Card?>(null) }

    val cards by viewModel.cards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(cards) {
        filteredCards.value = cards
    }

    if (loading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Cabeçalho", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = null)
                BasicTextField(
                    value = textState.value,
                    onValueChange = {
                        textState.value = it
                        filteredCards.value = if (it.isEmpty()) {
                            cards
                        } else {
                            cards.filter { card ->
                                card.name.contains(it, ignoreCase = true)
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                    })
                )
            }
            error?.let {
                Text(it, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredCards.value) { card ->
                    CardView(card = card, onClick = {
                        selectedCard.value = card
                        modalVisible.value = true
                    })
                }
            }
        }
    }

    if (modalVisible.value) {
        selectedCard.value?.let {
            CustomModal(card = it, onClose = { modalVisible.value = false })
        }
    }
}
