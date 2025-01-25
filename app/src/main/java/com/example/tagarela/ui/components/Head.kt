package com.example.tagarela.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tagarela.R
import com.example.tagarela.ui.theme.Orange

@Composable
fun Head() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Orange)
        .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_small),
            contentDescription = null,
            modifier = Modifier
                .height(50.dp)
                .width(130.dp)
        )
    }
}