package com.example.tagarela.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.tagarela.BuildConfig
import com.example.tagarela.data.models.Card
import com.example.tagarela.ui.theme.Purple40

@Composable
fun CardView(card: Card, isLarge: Boolean = false, onClick: () -> Unit) {
    val imageUrl = "${BuildConfig.BASE_IMG_URL}${card.image}"

    Card(
        modifier = Modifier
            .width(if (isLarge) 150.dp else 115.dp)
            .height(if (isLarge) 170.dp else 136.dp)
            .padding(bottom = 16.dp)
            .clickable { onClick() }
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(if (isLarge) 80.dp else 64.dp)
            )
            Text(
                card.name,
                style = TextStyle(
                    color = Purple40,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isLarge) 18.sp else 14.sp
                ),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
