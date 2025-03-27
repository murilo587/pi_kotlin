package com.example.tagarela.ui.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.ui.viewmodel.AccountViewModel
import android.util.Log

class AccountViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun MyAccountScreen(navHostController: NavHostController, userId: String) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context))
    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("MyAccountScreen", "Launching effect to get user data")
        viewModel.getUserData(userId)
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
                                if (user != null) {
                                    Log.d("MyAccountScreen", "User data: ${user?.username}, ${user?.email}")
                                    UserInfoItem(label = "Nome", info = user!!.username)
                                    UserInfoItem(label = "E-mail", info = user!!.email)
                                    UserInfoItem(label = "Senha", info = "**********")
                                } else {
                                    Log.d("MyAccountScreen", "User data is null")
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
                                        navHostController.navigate("editaccount/$userId")
                                    }
                            )
                        }
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
