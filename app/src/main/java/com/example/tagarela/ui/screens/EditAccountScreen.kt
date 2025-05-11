package com.example.tagarela.ui.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagarela.ui.theme.Orange
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.ui.components.Head
import com.example.tagarela.ui.components.Menu
import com.example.tagarela.ui.viewmodel.AccountViewModel
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.ui.viewmodel.AccountViewModelFactory

@Composable
fun EditAccountScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context, userPreferences))
    val updateResult by viewModel.updateResult.collectAsState()
    val userName by userPreferences.userName.collectAsState(initial = null)
    val userId by userPreferences.userId.collectAsState(initial = null)

    var username by remember { mutableStateOf(userName ?: "") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(updateResult) {
        Log.d("EditAccountScreen", "🔍 updateResult recebido: $updateResult")

        updateResult?.let {
            if (it.success) {
                navHostController.popBackStack()
            } else {
                errorMessage = it.error ?: "Erro ao atualizar usuário"
            }
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
                            text = "EDITAR PERFIL",
                            style = TextStyle(color = Color.Black, fontSize = 25.sp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 20.dp),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                        Column(modifier = Modifier.padding(top = 20.dp)) {
                            TextField(
                                value = username,
                                onValueChange = { username = it; usernameError = it.isBlank() },
                                label = { Text("Nome") },
                                isError = usernameError,
                                placeholder = { Text(userName ?: "Digite seu nome") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (usernameError) Color.Red else Color.Transparent)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            TextField(
                                value = password,
                                onValueChange = { password = it; passwordError = it.isBlank() },
                                label = { Text("Senha") },
                                isError = passwordError,
                                placeholder = { Text("**********") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (passwordError) Color.Red else Color.Transparent),
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Spacer(modifier = Modifier.height(60.dp))
                            Button(
                                onClick = {
                                    usernameError = username.isBlank()
                                    passwordError = password.isBlank()

                                    if (!usernameError && !passwordError) {
                                        if (!userId.isNullOrBlank()) {
                                            Log.d("EditAccountScreen", "Botão Confirmar clicado")
                                            viewModel.updateUser(username, password)
                                        } else {
                                            errorMessage = "Erro: ID do usuário não encontrado"
                                            Log.e("EditAccountScreen", "Erro: ID do usuário não encontrado")
                                        }
                                    } else {
                                        errorMessage = "Todos os campos devem ser preenchidos"
                                        Log.e("EditAccountScreen", "Todos os campos devem ser preenchidos")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Orange,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("EDITAR", fontSize = 18.sp)
                            }
                            if (errorMessage.isNotBlank()) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = errorMessage,
                                    color = Color.Red,
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = { Menu(navHostController) }
    )
}