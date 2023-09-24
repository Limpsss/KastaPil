package com.example.kastapil

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

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
            CreateGameScreen(navController)
        }
    }
}