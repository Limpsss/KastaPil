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
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                },
                navArgument("password") {
                    type = NavType.StringType
                })

        ){
            val username = it.arguments?.getString("username").toString()
            val password = it.arguments?.getString("password").toString()

            HomeScreen(navController, username, password)
        }

        composable(
            route = Screen.CreateGame.route
        ){
            CreateGameScreen()
        }
    }
}