package com.example.tagarela.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.viewmodel.CardViewModel
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.ui.components.CustomModal
import com.example.tagarela.ui.components.CardView
import com.example.tagarela.ui.theme.Orange
import com.example.tagarela.ui.theme.OrangeLight
import com.example.tagarela.utils.removeAccents

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: CardViewModel = viewModel(factory = CardViewModel.Factory(CardRepository(context)))

    val categorias = listOf("Alimento", "Ação", "Emoção", "Necessidade", "Meus Cartões")
    var categoriaSelecionada by remember { mutableStateOf<String?>(null) }

    val modalVisible = remember { mutableStateOf(false) }
    val selectedCardId = remember { mutableStateOf<String?>(null) }

    val cards by viewModel.cards.collectAsState()
    val recentCards by viewModel.recentCards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecentCards()
    }

    Scaffold(
        topBar = { Head() },
        bottomBar = { Menu(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(categorias.size) { index ->
                                val isSelected = categoriaSelecionada == categorias[index]

                                Button(
                                    onClick = {
                                        categoriaSelecionada =
                                            if (isSelected) null else categorias[index]
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .height(60.dp)
                                        .width(210.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) Orange else OrangeLight,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = categorias[index].uppercase(), style = MaterialTheme.typography.titleLarge)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        val cardsFiltrados = cards.filter { card ->
                            categoriaSelecionada?.removeAccents()
                                .equals(card.category.removeAccents(), ignoreCase = true)
                        }

                        if (categoriaSelecionada != null) {
                            Text(
                                text = categoriaSelecionada!!,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(cardsFiltrados.size) { index ->
                                    val card = cardsFiltrados[index]
                                    CardView(
                                        card = card,
                                        onClick = {
                                            selectedCardId.value = card.id
                                            modalVisible.value = true
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Text(
                            text = "Recentes",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (recentCards.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 5.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(30.dp),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                items(recentCards.take(4).size) { index ->
                                    val card = recentCards[index]
                                    CardView(
                                        card = card,
                                        onClick = {
                                            selectedCardId.value = card.id
                                            modalVisible.value = true
                                        }
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Nenhum card recente disponível 😢",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    )

    if (modalVisible.value && selectedCardId.value != null) {
        CustomModal(cardId = selectedCardId.value!!, onClose = { modalVisible.value = false }, viewModel = viewModel)
    }
}
