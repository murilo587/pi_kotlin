package com.example.tagarela.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.viewmodel.CardViewModel
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.ui.components.CustomModal
import com.example.tagarela.ui.components.CardView
import com.example.tagarela.ui.theme.Orange
import com.example.tagarela.ui.theme.OrangeLight
import com.example.tagarela.utils.removeAccents

@OptIn(ExperimentalLayoutApi::class)
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
    val mostUsedCards by viewModel.mostUsedCards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecentCards()
        viewModel.fetchMostUsedCards()
    }

    Scaffold(
        topBar = { Head() },
        bottomBar = { Menu(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        item {
                            IconButton(
                                onClick = { navController.navigate("search") },
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_search_purple),
                                    contentDescription = "Ícone de Pesquisa",
                                    modifier = Modifier.size(37.dp)
                                )
                            }
                        }

                        item {
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
                                        Text(
                                            text = categorias[index].uppercase(),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        if (categoriaSelecionada != null) {
                            val cardsFiltrados = cards.filter { card ->
                                categoriaSelecionada?.removeAccents()
                                    .equals(card.category.removeAccents(), ignoreCase = true)
                            }

                            item {
                                Text(
                                    text = categoriaSelecionada!!.uppercase(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            if (cardsFiltrados.isEmpty()) {
                                item {
                                    Text(
                                        text = "Nenhum card disponível 😢",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .padding(16.dp)
                                    )
                                }
                            } else {
                                item {
                                    FlowRow(
                                        maxItemsInEachRow = 2,
                                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                                        verticalArrangement = Arrangement.spacedBy(15.dp),
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                    ) {
                                        cardsFiltrados.forEach { card ->
                                            CardView(
                                                card = card,
                                                isLarge = true,
                                                onClick = {
                                                    selectedCardId.value = card.id
                                                    modalVisible.value = true
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "RECENTES",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                if (recentCards.isNotEmpty()) {
                                    FlowRow(
                                        maxItemsInEachRow = 2,
                                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                                        verticalArrangement = Arrangement.spacedBy(15.dp),
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                    ) {
                                        recentCards.take(4).forEach { card ->
                                            CardView(
                                                card = card,
                                                isLarge = true,
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
                                        modifier = Modifier
                                            .padding(16.dp)
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "MAIS UTILIZADOS",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                if (mostUsedCards.isNotEmpty()) {
                                    FlowRow(
                                        maxItemsInEachRow = 2,
                                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                                        verticalArrangement = Arrangement.spacedBy(15.dp),
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                                    ) {
                                        mostUsedCards.take(4).forEach { card ->
                                            CardView(
                                                card = card,
                                                isLarge = true,
                                                onClick = {
                                                    selectedCardId.value = card.id
                                                    modalVisible.value = true
                                                }
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "Nenhum card utilizado disponível 😢",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
        }
    )

    if (modalVisible.value && selectedCardId.value != null) {
        CustomModal(
            cardId = selectedCardId.value!!,
            onClose = { modalVisible.value = false },
            viewModel = viewModel
        )
    }
}
