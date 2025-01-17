package com.example.tagarela.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tagarela.ui.screens.Screen1
import com.example.tagarela.ui.screens.Screen2

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "screen1", modifier = modifier) {
        composable("screen1") { Screen1(navController) }
        composable("screen2") { Screen2(navController) }
    }
}
