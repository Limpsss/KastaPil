package com.example.kastapil.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kastapil.models.GameInfo
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.models.AppearanceManager
import com.example.kastapil.ui.screens.CreateGameScreen
import com.example.kastapil.ui.screens.HomeScreen
import com.example.kastapil.ui.screens.InGameScreen
import com.example.kastapil.ui.screens.SettingsScreen
import com.example.kastapil.ui.screens.SignInScreen
import com.example.kastapil.ui.screens.StatisticsScreen
import com.example.kastapil.viewModels.CreateGameViewModel
import com.example.kastapil.viewModels.HomeViewModel
import com.example.kastapil.viewModels.HomeViewModelFactory
import com.example.kastapil.viewModels.InGameViewModel
import com.example.kastapil.viewModels.InGameViewModelFactory
import com.example.kastapil.viewModels.SettingsViewModel
import com.example.kastapil.viewModels.SettingsViewModelFactory
import com.example.kastapil.viewModels.SignInViewModel
import com.example.kastapil.viewModels.SignInViewModelFactory
import com.example.kastapil.viewModels.StatisticsViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Composable
fun SetUpNavHost(
    navController: NavHostController,
    googleAuthClient: GoogleAuthClient,
    appearanceManager: AppearanceManager
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Statistics.route
    ){

        // Sign in screen
        composable(
            route = Screen.SignIn.route
        ) {
            val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(googleAuthClient))
            SignInScreen(
                navController = navController,
                viewModel = signInViewModel,
            )
        }

        // Home screen
        composable(
            route = Screen.Home.route,
        ){
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(googleAuthClient))
            HomeScreen(navController, homeViewModel)
        }

        // Create game screen
        composable(
            route = Screen.CreateGame.route
        ){
            val createGameViewModel: CreateGameViewModel = viewModel()
            CreateGameScreen(navController, createGameViewModel)
        }

        // In game screen
        composable(
            route = Screen.inGame.route,
            arguments = listOf(navArgument("gameInfo") {type = NavType.StringType})
        ){
            // JSON string from argument
            val jsonGameInfo: String? = it.arguments?.getString("gameInfo")
            // The type i want to deserialize to
            val type: Type = object : TypeToken<GameInfo>() {}.type
            // Deserialize the JSON string to a Map<String, String>
            val gameInfo: GameInfo = Gson().fromJson(jsonGameInfo, type)
            //val inGameViewModel: InGameViewModel = viewModel()
            val inGameViewModel: InGameViewModel =
                viewModel(factory = InGameViewModelFactory(gameInfo)) // pass param like this
            InGameScreen(navController, inGameViewModel)
        }

        // Settings screen
        composable(
            route = Screen.Settings.route
        ){
            val settingsViewModel: SettingsViewModel =
                viewModel(factory = SettingsViewModelFactory(appearanceManager))
            SettingsScreen(settingsViewModel, navController)
        }

        // Statistics screen
        composable(
            route = Screen.Statistics.route
        ){
            val statisticsViewModel: StatisticsViewModel = viewModel()
            StatisticsScreen(statisticsViewModel, navController)
        }
    }
}