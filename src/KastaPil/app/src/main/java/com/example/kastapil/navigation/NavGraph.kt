package com.example.kastapil.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kastapil.models.GameInfo
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.ui.screens.CreateGameScreen
import com.example.kastapil.ui.screens.HomeScreen
import com.example.kastapil.ui.screens.InGameScreen
import com.example.kastapil.ui.screens.SignInScreen
import com.example.kastapil.viewModels.CreateGameViewModel
import com.example.kastapil.viewModels.HomeViewModel
import com.example.kastapil.viewModels.HomeViewModelFactory
import com.example.kastapil.viewModels.InGameViewModel
import com.example.kastapil.viewModels.InGameViewModelFactory
import com.example.kastapil.viewModels.SignInViewModel
import com.example.kastapil.viewModels.SignInViewModelFactory
import com.google.android.gms.auth.api.identity.Identity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@Composable
fun SetUpNavHost(
    navController: NavHostController,
    googleAuthClient: GoogleAuthClient
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ){
        composable(
            route = Screen.SignIn.route
        ) {
            val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(googleAuthClient))
            SignInScreen(
                navController = navController,
                viewModel = signInViewModel,
            )
        }

        composable(
            route = Screen.Home.route,
        ){
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(googleAuthClient))
            HomeScreen(navController, homeViewModel)
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
    }
}