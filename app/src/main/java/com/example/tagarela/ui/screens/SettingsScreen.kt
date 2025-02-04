package com.example.tagarela.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu

@Composable
fun SettingsScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = { Head() },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(40.dp)) {
                    Column {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "CONFIGURAÇÕES",
                            style = TextStyle(color = Color.Black, fontSize = 25.sp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 20.dp),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                    Column(modifier = Modifier.padding(top = 30.dp)) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .clickable {},
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "SUA CONTA",
                                    style = TextStyle(color = Color.Black, fontSize = 20.sp),
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(15.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .clickable {},
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_eye),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "PROGRESSO",
                                    style = TextStyle(color = Color.Black, fontSize = 20.sp),
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(15.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .clickable {},
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_notification),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "NOTIFICAÇÃO",
                                    style = TextStyle(color = Color.Black, fontSize = 20.sp),
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(15.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { Menu() }
    )
}
