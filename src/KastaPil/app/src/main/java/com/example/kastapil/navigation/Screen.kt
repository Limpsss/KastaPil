package com.example.kastapil.navigation

import android.util.Log
import com.example.kastapil.models.GameInfo
import com.google.gson.Gson

const val GAME_INFO_ARGUMENT = "gameInfo"
sealed class Screen (val route: String) {
    object SignIn: Screen(route = "sign_in_screen")
    object Home: Screen(route = "home_screen/{username}/{password}")
    object CreateGame: Screen(route = "create_game_screen")
    object inGame: Screen(route = "in_game_screen/{$GAME_INFO_ARGUMENT}") {
        fun passGameInfo(gameInfo: GameInfo): String {
            val jsonGameInfo: String = Gson().toJson(gameInfo)
            return "in_game_screen/$jsonGameInfo"
        }
    }
    object Settings: Screen(route = "settings_screen")
}
