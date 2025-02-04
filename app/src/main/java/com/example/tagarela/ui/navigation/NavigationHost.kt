package com.example.tagarela.ui.navigation

import SearchScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tagarela.ui.screens.SignInScreen
import com.example.tagarela.ui.screens.SettingsScreen
import com.example.tagarela.ui.screens.SignUpScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "settings", modifier = modifier) {
        composable("signin") { SignInScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("settings") { SettingsScreen(navController)}
        composable("signup") { SignUpScreen(navController)}
    }
}
