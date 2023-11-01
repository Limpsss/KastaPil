package com.example.kastapil.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kastapil.ui.screens.CreateGameScreen
import com.example.kastapil.ui.screens.HomeScreen
import com.example.kastapil.ui.screens.SignInScreen
import com.example.kastapil.viewModels.CreateGameViewModel

@Composable
fun SetUpNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ){

        composable(
            route = Screen.SignIn.route
        ){
            SignInScreen(navController)
        }

        composable(
            route = Screen.Home.route,
        ){

            HomeScreen(navController)
        }

        composable(
            route = Screen.CreateGame.route
        ){
            val createGameViewModel: CreateGameViewModel = viewModel()
            CreateGameScreen(navController, createGameViewModel)
        }
    }
}