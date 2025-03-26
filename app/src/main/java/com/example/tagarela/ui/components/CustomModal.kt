package com.example.tagarela.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagarela.data.models.Card
import com.example.tagarela.ui.theme.Orange
import kotlinx.coroutines.launch

@Composable
fun CustomModal(card: Card, onClose: () -> Unit) {
    val syllables = card.syllables.split("-")
    val offsetY = remember { Animatable(1600f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {},
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .offset { IntOffset(0, offsetY.value.toInt()) }
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.name,
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Detalhes do cartão",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    syllables.forEach { syllable ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(Orange, shape = RoundedCornerShape(4.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = syllable)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            offsetY.animateTo(
                                targetValue = 1800f,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                )
                            )
                            onClose()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}
