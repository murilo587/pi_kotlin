package com.example.tagarela.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tagarela.data.UserPreferences
import com.example.tagarela.ui.screens.SignInScreen
import com.example.tagarela.ui.screens.SettingsScreen
import com.example.tagarela.ui.screens.SignUpScreen
import com.example.tagarela.ui.screens.MyAccountScreen
import com.example.tagarela.ui.screens.EditAccountScreen
import com.example.tagarela.ui.screens.QueueScreen
import com.example.tagarela.ui.screens.GameScreen
import com.example.tagarela.ui.screens.SearchScreen
import com.example.tagarela.ui.screens.HomeScreen
import com.example.tagarela.ui.screens.RegisterCardScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    var isLogged by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(true) {
        userPreferences.isLogged.collect { loggedStatus ->
            isLogged = loggedStatus
        }
    }

    if (isLogged == null) {
        return
    }

    val startDestination = if (isLogged == true) {
        "home"
    } else {
        "signin"
    }

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,

            ) {
            composable("signin") { SignInScreen(navController) }
            composable("search") { SearchScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
            composable("signup") { SignUpScreen(navController) }
            composable("queue") { QueueScreen(navController) }
            composable("game") { GameScreen(navController) }
            composable("home") { HomeScreen(navController) }
            composable("registercard") { RegisterCardScreen(navController) }
            composable("editaccount") { EditAccountScreen(navController) }
            composable("myaccount") { MyAccountScreen(navController) }
        }
    }




