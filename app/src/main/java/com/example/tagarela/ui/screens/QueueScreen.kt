package com.example.tagarela.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.tagarela.R
import com.example.tagarela.data.models.Card
import com.example.tagarela.ui.viewmodel.CardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tagarela.ui.components.Head
import com.example.tagarela.data.repository.CardRepository
import com.example.tagarela.ui.components.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagarela.BuildConfig
import com.example.tagarela.ui.theme.PurpleMain
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.example.tagarela.ui.components.Menu

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
fun QueueScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: CardViewModel = viewModel(factory = CardViewModelFactory(CardRepository(context)))

    val textState = remember { mutableStateOf("") }
    val filteredCards = remember { mutableStateOf(listOf<Card>()) }
    val queue = remember { mutableStateListOf<Card>() }
    val baseUrl = BuildConfig.BASE_IMG_URL

    val cards by viewModel.cards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(cards, textState.value) {
        filteredCards.value = if (textState.value.isEmpty()) {
            cards
        } else {
            cards.filter { card ->
                card.name.contains(textState.value, ignoreCase = true)
            }
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .padding(bottom = 16.dp)
                                .background(Color.White)
                                .border(2.dp, PurpleMain, MaterialTheme.shapes.medium)
                                .padding(horizontal = 8.dp, vertical = 35.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FlowRow(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp),
                                    mainAxisSpacing = 8.dp,
                                    crossAxisSpacing = 8.dp,
                                    crossAxisAlignment = FlowCrossAxisAlignment.Center,
                                    mainAxisAlignment = FlowMainAxisAlignment.Center
                                ) {
                                    val cardSize = if (queue.size > 3) (180 / queue.size).dp else 60.dp

                                    queue.forEach { card ->
                                        Box(
                                            modifier = Modifier
                                                .size(cardSize)
                                                .align(Alignment.CenterVertically)
                                        ) {
                                            Image(
                                                painter = rememberImagePainter(data = baseUrl + card.image),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(130.dp)
                                                    .clip(MaterialTheme.shapes.medium)
                                                    .clickable {
                                                        queue.remove(card)
                                                    }
                                            )
                                        }
                                    }
                                }
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = null,
                                    tint = PurpleMain,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable { queue.clear() }
                                        .padding(start = 8.dp)
                                )
                            }
                        }

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
                                        onValueChange = {
                                            textState.value = it
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(PurpleMain)
                                            .padding(horizontal = 8.dp, vertical = 12.dp),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(onSearch = {
                                        }),
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
                            Text(
                                it,
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(top = 8.dp)
                        ) {
                            item {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    mainAxisSpacing = 8.dp,
                                    crossAxisSpacing = 8.dp,
                                    crossAxisAlignment = FlowCrossAxisAlignment.Start,
                                    mainAxisAlignment = FlowMainAxisAlignment.Center,
                                ) {
                                    filteredCards.value.forEach { card ->
                                        CardView(
                                            card = card,
                                            onClick = {
                                                if (queue.size < 5) {
                                                    queue.add(card)

                                                    val audioUri = baseUrl + card.audio

                                                    try {
                                                        val mediaPlayer = MediaPlayer()
                                                        mediaPlayer.setDataSource(audioUri)
                                                        mediaPlayer.prepare()
                                                        mediaPlayer.setOnCompletionListener {
                                                            mediaPlayer.release()
                                                        }
                                                        mediaPlayer.start()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                        println("Erro ao reproduzir o áudio: ${e.message}")
                                                    }

                                                }
                                            }
                                        )
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
