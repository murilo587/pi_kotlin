package com.example.tagarela.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import com.example.tagarela.R
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.data.models.User
import com.example.tagarela.data.repository.UserRepository

@Composable
fun MyAccountScreen(navHostController: NavHostController, userId: String) {
    val context = LocalContext.current
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        val repository = UserRepository(context)
        val userResult = repository.getUserData(userId)
        if (userResult.success) {
            user = userResult.user
        } else {
        }
    }

    Scaffold(
        topBar = { Head() },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(40.dp)) {
                    Column {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "SUA CONTA",
                            style = TextStyle(color = Color.Black, fontSize = 25.sp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 20.dp),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                user?.let {
                                    UserInfoItem(label = "Nome", info = it.username)
                                    UserInfoItem(label = "E-mail", info = it.email)
                                    UserInfoItem(label = "Senha", info = "**********")
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        navHostController.navigate("")
                                    }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { Menu(navHostController) }
    )
}

@Composable
fun UserInfoItem(label: String, info: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = label,
            style = TextStyle(color = Color.Black, fontSize = 24.sp)
        )
        Text(
            text = info,
            style = TextStyle(color = Color.DarkGray, fontSize = 20.sp),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
