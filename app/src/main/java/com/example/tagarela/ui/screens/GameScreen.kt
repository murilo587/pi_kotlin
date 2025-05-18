package com.example.tagarela.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.tagarela.BuildConfig
import com.example.tagarela.R
import com.example.tagarela.ui.viewmodel.GameViewModel
import com.example.tagarela.data.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.ui.theme.Purple40

@Composable
fun GameScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = GameViewModelFactory(GameRepository(context))
    val viewModel: GameViewModel = viewModel(factory = factory)

    val gameResponse by viewModel.gameResponse.collectAsState()
    val games = gameResponse?.games ?: emptyList()
    val currentGameIndex by viewModel.currentGameIndex.collectAsState()
    val isQuizFinished by viewModel.isQuizFinished.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val baseUrl = BuildConfig.BASE_IMG_URL
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    var buttonColors by remember {
        mutableStateOf(
            listOf(
                Color(0xFFFFE647),
                Color(0xFFFA0000),
                Color(0xFF1A7BF2),
                Color(0xFF494949)
            )
        )
    }
    var imageAlpha by remember { mutableStateOf(1f) }
    var cardSizes by remember { mutableStateOf(List(4) { 120.dp }) }
    var showCongratsDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    fun playSound(context: Context, soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.apply {
            setOnCompletionListener { release() }
            start()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadGameData()
    }

    LaunchedEffect(isQuizFinished) {
        if (isQuizFinished) {
            showCongratsDialog = true
            playSound(context, R.raw.game_victory)
            coroutineScope.launch {
                delay(4000)
                showCongratsDialog = false
                navController.navigate("settings")
            }
        }
    }

    if (showCongratsDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.congrats_image),
                        contentDescription = "Imagem de Parabéns",
                        modifier = Modifier.size(130.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Parabéns", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Você concluiu o Quiz!", fontSize = 15.sp)
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            showCongratsDialog = false
                            navController.navigate("settings")
                        },
                        modifier = Modifier.width(120.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("PRONTO")
                    }
                }
            }

        )
    }

    Scaffold(
        topBar = { Head() },
        bottomBar = { Menu(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                if (loading) {
                    Text(text = "Carregando...", fontSize = 18.sp)
                } else if (games.isNotEmpty()) {
                    val currentGame = games.getOrNull(currentGameIndex)

                    if (currentGame != null) {
                        Text(
                            text = "Nível 1 - Questão ${currentGameIndex + 1}/${games.size}",
                            fontSize = 18.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            thickness = 2.dp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        AndroidView(
                            factory = { context ->
                                VideoView(context).apply {
                                    setOnPreparedListener { mediaPlayer ->
                                        mediaPlayer.seekTo(0)
                                        mediaPlayer.start()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            update = { videoView ->
                                val videoUri = Uri.parse(
                                    baseUrl + (currentGame.items.getOrNull(currentGame.correctAnswer)?.video
                                        ?: "")
                                )

                                videoView.stopPlayback()
                                videoView.setVideoURI(videoUri)
                                videoView.requestFocus()
                                videoView.start()
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            listOf(0 to 2, 2 to 4).forEach { (start, end) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    currentGame.items.subList(start, end)
                                        .forEachIndexed { index, item ->
                                            val actualIndex = start + index
                                            val animatedSize by animateDpAsState(
                                                targetValue = if (selectedAnswerIndex == actualIndex) 140.dp else 120.dp,
                                                animationSpec = tween(durationMillis = 1000),
                                                label = "Card Size Animation"
                                            )
                                            val animatedWidth by animateDpAsState(
                                                targetValue = if (selectedAnswerIndex == actualIndex) 170.dp else 150.dp,
                                                animationSpec = tween(durationMillis = 1000),
                                                label = "Card Width Animation"
                                            )

                                            Card(
                                                modifier = Modifier
                                                    .height(animatedSize)
                                                    .width(animatedWidth)
                                                    .clickable {
                                                        selectedAnswerIndex = actualIndex
                                                        val isCorrect =
                                                            actualIndex == currentGame.correctAnswer

                                                        buttonColors =
                                                            buttonColors.mapIndexed { i, color ->
                                                                if (i == actualIndex) {
                                                                    if (isCorrect) Color.Green else Color.Red
                                                                } else color
                                                            }

                                                        playSound(
                                                            context,
                                                            if (isCorrect) R.raw.correct_answer else R.raw.wrong_answer
                                                        )

                                                        imageAlpha = 0.5f

                                                        coroutineScope.launch {
                                                            delay(200)
                                                            imageAlpha = 1f
                                                            delay(1000)
                                                            selectedAnswerIndex = -1
                                                            delay(2000)
                                                            if (isCorrect) {
                                                                viewModel.nextGame()
                                                            }
                                                            buttonColors = listOf(
                                                                Color(0xFFFFE647),
                                                                Color(0xFFFA0000),
                                                                Color(0xFF1A7BF2),
                                                                Color(0xFF494949)
                                                            )
                                                        }
                                                    },
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(containerColor = buttonColors[actualIndex])
                                            ) {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Image(
                                                        painter = rememberImagePainter(baseUrl + item.image),
                                                        contentDescription = "Alternativa ${actualIndex + 1}",
                                                        modifier = Modifier
                                                            .size(90.dp)
                                                            .graphicsLayer(alpha = imageAlpha)
                                                    )
                                                }
                                            }
                                        }
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "Erro ao carregar os dados", color = Color.Red, fontSize = 18.sp)
                }
            }
        }
    }
}

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
