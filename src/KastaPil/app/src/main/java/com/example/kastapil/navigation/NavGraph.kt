package com.example.kastapil.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kastapil.ui.screens.CreateGameScreen
import com.example.kastapil.ui.screens.HomeScreen
import com.example.kastapil.ui.screens.InGameScreen
import com.example.kastapil.ui.screens.SignInScreen
import com.example.kastapil.viewModels.CreateGameViewModel
import com.example.kastapil.viewModels.InGameViewModel
import com.google.gson.Gson

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

        composable(
            route = Screen.inGame.route,
            arguments = listOf(navArgument("gameInfo") {type = NavType.StringType})
        ){
            val jsonGameInfo = it.arguments?.getString("gameInfo")
            val gameInfo = Gson().fromJson(jsonGameInfo, Map::class.java)

            val inGameViewModel: InGameViewModel = viewModel()
            InGameScreen(gameInfo, navController, inGameViewModel)
        }
    }
}