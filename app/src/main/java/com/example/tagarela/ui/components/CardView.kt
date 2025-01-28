package com.example.tagarela.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.tagarela.data.models.Card

@Composable
fun CardView(card: Card, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(115.dp)
            .height(130.dp)
            .padding(bottom = 16.dp)
            .clickable { onClick() },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(data = card.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
            Text(card.name, modifier = Modifier.padding(start = 8.dp))
        }
    }
}
