package com.example.tagarela.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.ui.theme.Purple40

@Composable
fun Menu(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .background(Purple40)
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Home icon",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                )
            }
            Button(
                onClick = {navHostController.navigate("queue")},
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_cards),
                    contentDescription = "Cards icon",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                )
            }
            Button(
                onClick = {navHostController.navigate("gamemenu")},
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_quiz),
                    contentDescription = "Quiz icon",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                )
            }
            Button(
                onClick = {navHostController.navigate("settings")},
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = "Settings icon",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                )
            }
        }
    }
}
