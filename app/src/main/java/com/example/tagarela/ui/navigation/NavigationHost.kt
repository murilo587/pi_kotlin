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
import com.example.tagarela.ui.screens.MyAccountScreen
import com.example.tagarela.ui.screens.EditAccountScreen
import com.example.tagarela.ui.screens.QueueScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "queue", modifier = modifier) {
        composable("signin") { SignInScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("settings") { SettingsScreen(navController)}
        composable("signup") { SignUpScreen(navController)}
        composable("queue") { QueueScreen(navController)}
        composable("myaccount/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            userId?.let { MyAccountScreen(navController, it) }
        }
        composable("editaccount/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            userId?.let { EditAccountScreen(navController, it) }
        }
    }
}

