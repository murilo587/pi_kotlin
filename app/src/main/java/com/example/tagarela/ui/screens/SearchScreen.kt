package com.example.tagarela.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.data.models.Card
import com.example.tagarela.ui.viewmodel.CardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.CustomModal
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.components.CardView
import com.example.tagarela.ui.theme.PurpleMain
import com.example.tagarela.ui.components.Menu

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: CardViewModel = viewModel(factory = CardViewModel.Factory(CardRepository(context)))

    val textState = remember { mutableStateOf("") }
    val filteredCards = remember { mutableStateOf(listOf<Card>()) }
    val modalVisible = remember { mutableStateOf(false) }
    val selectedCardId = remember { mutableStateOf<String?>(null) }

    val cards by viewModel.cards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(cards, textState.value) {
        filteredCards.value = cards.filter { card ->
            textState.value.isEmpty() || card.name.contains(textState.value, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = { Head() },
        bottomBar = { Menu(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(PurpleMain, shape = MaterialTheme.shapes.extraLarge)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().background(PurpleMain)
                                ) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_search),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    BasicTextField(
                                        value = textState.value,
                                        onValueChange = { textState.value = it },
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(PurpleMain)
                                            .padding(horizontal = 8.dp, vertical = 12.dp),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(onSearch = {}),
                                        textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
                                        cursorBrush = SolidColor(Color.Black),
                                        decorationBox = { innerTextField ->
                                            if (textState.value.isEmpty()) {
                                                Text(
                                                    text = "Search",
                                                    style = TextStyle(color = Color.White, fontSize = 20.sp)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                            }
                        }

                        error?.let {
                            Text(it, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            items(filteredCards.value.size) { index ->
                                val card = filteredCards.value[index]
                                CardView(
                                    card = card,
                                    onClick = {
                                        selectedCardId.value = card.id
                                        modalVisible.value = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    if (modalVisible.value && selectedCardId.value != null) {
        CustomModal(cardId = selectedCardId.value!!, onClose = { modalVisible.value = false }, viewModel = viewModel)
    }
}
