package com.example.tagarela.ui.components

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tagarela.BuildConfig
import com.example.tagarela.R
import com.example.tagarela.data.models.Card
import com.example.tagarela.ui.theme.DarkGray
import com.example.tagarela.ui.theme.Orange
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomModal(card: Card, onClose: () -> Unit) {
    val syllables = card.syllables.split("-")
    val offsetY = remember { Animatable(1600f) }
    val coroutineScope = rememberCoroutineScope()
    val baseUrl = BuildConfig.BASE_IMG_URL
    val context = LocalContext.current


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
                .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.name.uppercase(),
                    style = TextStyle(
                        color = DarkGray,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            val videoUri = Uri.parse(baseUrl + card.video)
                            setVideoURI(videoUri)

                            val audioManager =
                                context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                0
                            )

                            start()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    syllables.forEach { syllable ->
                        Box(
                            modifier = Modifier
                                .background(Orange, shape = RoundedCornerShape(4.dp))
                                .padding(14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = syllable.uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight(weight = 600),
                                fontSize = 22.sp
                            )
                        }
                    }
                }

                Image(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "Play icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            val audioUri = Uri.parse(baseUrl + card.audio)
                            MediaPlayer.create(context, audioUri)?.apply {
                                setOnCompletionListener { release() }
                                start()
                            }
                        }
                )

                Spacer(modifier = Modifier.height(40.dp))

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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 170.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "PRONTO",
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}
