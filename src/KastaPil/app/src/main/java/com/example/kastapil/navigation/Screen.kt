package com.example.kastapil.navigation

sealed class Screen (val route: String) {
    object SignIn: Screen(route = "sign_in_screen")
    object Home: Screen(route = "home_screen/{username}/{password}")
    object CreateGame: Screen(route = "create_game_screen")
}
