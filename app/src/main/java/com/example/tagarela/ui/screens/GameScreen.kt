package com.example.tagarela.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tagarela.data.repository.GameRepository
import com.example.tagarela.ui.viewmodel.GameViewModel

@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = GameRepository(context)
    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(repository)
    )
    val gameState by viewModel.gameState.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()

    if (gameState.success && !gameState.data.isNullOrEmpty()) {
        val currentQuestion = gameState.data!![currentQuestionIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nível ${currentQuestion.level}",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Questão ${currentQuestionIndex + 1}/${gameState.data!!.size}",
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                currentQuestion.gameItems.forEachIndexed { index, item ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { viewModel.onAnswerSelected(index) },
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Item ${index + 1}",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
