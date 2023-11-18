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
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.ui.screens.CreateGameScreen
import com.example.kastapil.ui.screens.HomeScreen
import com.example.kastapil.ui.screens.InGameScreen
import com.example.kastapil.ui.screens.SignInScreen
import com.example.kastapil.viewModels.CreateGameViewModel
import com.example.kastapil.viewModels.InGameViewModel
import com.example.kastapil.viewModels.SignInViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun SetUpNavHost(
    navController: NavHostController,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context

) {
    val viewModel: SignInViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ){

        composable(
            route = Screen.SignIn.route
        ) {

            SignInScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }

        // TODO FIX THIS, IT USES SIGN IN VIEWMODEL
        composable(
            route = Screen.Home.route,
        ){
            HomeScreen(
                navController,
                userData = viewModel.googleAuthClient.getSignedInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        viewModel.googleAuthClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                    }
                })
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