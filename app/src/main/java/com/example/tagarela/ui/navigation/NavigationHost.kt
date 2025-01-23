package com.example.tagarela.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tagarela.ui.screens.Login
import com.example.tagarela.ui.screens.SearchScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "search", modifier = modifier) {
        composable("login") { Login() }
        composable("search") { SearchScreen(navController) }
    }
}
