package com.example.tagarela.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.ui.theme.Orange
import com.example.tagarela.ui.viewmodel.AccountViewModel
import com.example.tagarela.ui.viewmodel.AccountViewModelFactory

@Composable
fun MyAccountScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val viewlModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context, userPreferences))
    val userName by userPreferences.userName.collectAsState(initial = null)
    LaunchedEffect(userName) {
        Log.d("MyAccountScreen", "🔍 Nome do usuário carregado: $userName")
    }


    Scaffold(
        topBar = { Head() },
        bottomBar = { Menu(navHostController) },
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
                                if (userName != null) {
                                    UserInfoItem(label = "Nome", info = userName ?: "Nome não disponível")
                                    UserInfoItem(label = "Senha", info = "**********")
                                } else {
                                    Text(
                                        text = "Carregando...",
                                        style = TextStyle(color = Color.DarkGray, fontSize = 20.sp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        navHostController.navigate("editaccount")
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(200.dp))
                    Button(
                        onClick = { viewlModel.logout(navHostController) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .width(120.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("SAIR", fontSize = 18.sp)
                    }
                }
            }
        },
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
