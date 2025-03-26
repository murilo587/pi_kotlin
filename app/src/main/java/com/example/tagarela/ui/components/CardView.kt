package com.example.tagarela.ui.components

import com.example.tagarela.BuildConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.tagarela.data.models.Card
import com.example.tagarela.ui.theme.Purple40

@Composable
fun CardView(card: Card, onClick: () -> Unit) {
    val imageUrl = "${BuildConfig.BASE_IMG_URL}${card.image}"

    Card(
        modifier = Modifier
            .width(115.dp)
            .height(135.dp)
            .padding(bottom = 16.dp)
            .clickable { onClick() },
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
            .fillMaxSize()){
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
            Text(card.name, style = TextStyle(
                color = Purple40,
                fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(top = 10.dp))
        }
    }
}
