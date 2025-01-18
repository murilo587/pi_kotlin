package com.example.tagarela.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tagarela.R
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.tagarela.ui.theme.DarkGray
import com.example.tagarela.ui.theme.Orange
import com.example.tagarela.ui.theme.Purple40

@Composable
fun Login() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                .background(Color(0xFFFFA700)),
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
                    append("BEM-VINDO ")
                    withStyle(style = SpanStyle(color = Orange)) {
                        append("DE VOLTA")
                    }
                }.toAnnotatedString()

                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(26.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(70.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("ENTRAR", fontSize = 22.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "NÃO POSSUI CONTA?",
                        style = MaterialTheme.typography.bodyLarge.copy(color = DarkGray)
                    )
                }
            }
        }
    }
}
