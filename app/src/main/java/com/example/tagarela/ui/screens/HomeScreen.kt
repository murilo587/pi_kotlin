package com.example.tagarela.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.viewmodel.CardViewModel
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.utils.removeAccents

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: CardViewModel = viewModel(factory = CardViewModel.Factory(CardRepository(context)))

    val categorias = listOf("Alimento", "Ação", "Emoção", "Necessidade", "Meus Cartões")
    var categoriaSelecionada by remember { mutableStateOf<String?>(null) }

    val cards by viewModel.cards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

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
                                Button(
                                    onClick = {
                                        categoriaSelecionada =
                                            if (categoriaSelecionada == categorias[index]) null else categorias[index]
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Text(text = categorias[index])
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        error?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        val cardsFiltrados = cards.filter { card ->
                            categoriaSelecionada?.removeAccents()
                                .equals(card.category.removeAccents(), ignoreCase = true)
                        }

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            if (categoriaSelecionada != null && cardsFiltrados.isEmpty()) {
                                item {
                                    Text(
                                        text = "Não há cards disponíveis para essa categoria 😢",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                items(cardsFiltrados.size) { index ->
                                    val card = cardsFiltrados[index]
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "Categoria: ${card.category}", style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
