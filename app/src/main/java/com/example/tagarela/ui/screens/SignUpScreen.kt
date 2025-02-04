package com.example.tagarela.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tagarela.R
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.ui.theme.DarkGray
import com.example.tagarela.ui.theme.Orange
import com.example.tagarela.ui.theme.Purple40
import com.example.tagarela.data.repository.UserRepository
import com.example.tagarela.ui.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val signUpViewModel: SignUpViewModel = viewModel(factory = SignUpViewModelFactory(context))

    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    val signUpResult by signUpViewModel.signUpResult

    val userPreferences = UserPreferences(context)
    val userId by userPreferences.userId.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple40),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(90.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(280.dp)
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White, shape = RoundedCornerShape(topStart = 76.dp))
                    .padding(20.dp)
            ) {
                val annotatedString = AnnotatedString.Builder().apply {
                    append("BEM VINDO!")
                    withStyle(style = SpanStyle(color = Orange)) {
                        append(" :)")
                    }
                }.toAnnotatedString()

                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(26.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("APELIDO", fontSize = 18.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 13.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                )

                Spacer(modifier = Modifier.height(26.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("EMAIL", fontSize = 18.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 13.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                )

                Spacer(modifier = Modifier.height(26.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("SENHA", fontSize = 18.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 13.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(26.dp))

                TextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    label = { Text("REPITA A SENHA", fontSize = 18.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 13.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(70.dp))

                Button(
                    onClick = { signUpViewModel.signUp(nickname, email, password, repeatPassword) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("CADASTRAR", fontSize = 22.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                signUpResult?.let { result ->
                    if (result.success) {
                        Text("Cadastro bem-sucedido!")
                        LaunchedEffect(userId) {
                            if (userId != null) {
                                navHostController.navigate("search")
                            }
                        }
                        if (userId == null) {
                            Text("ID de usuário não encontrado. Por favor, tente novamente.")
                        }
                    } else {
                        Text("Falha no cadastro: ${result.error}")
                    }
                }

                TextButton(
                    onClick = { navHostController.navigate("login") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "JÁ POSSUI CONTA?",
                        style = MaterialTheme.typography.bodyLarge.copy(color = DarkGray)
                    )
                }
            }
        }
    }
}

class SignUpViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            val userRepository = UserRepository(context)
            val userPreferences = UserPreferences(context)
            return SignUpViewModel(userRepository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
