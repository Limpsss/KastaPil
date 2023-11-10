package com.example.kastapil.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.log

class CreateGameViewModel : ViewModel() {
    val is301Selected = mutableStateOf(true)
    val is501Selected = mutableStateOf(false)

    val isBo1Selected = mutableStateOf(true)
    val isBo3Selected = mutableStateOf(false)
    val isBo5Selected = mutableStateOf(false)

    val isPlayerOneSelected = mutableStateOf(true)
    val isPlayerTwoSelected = mutableStateOf(false)

    val playerOneName = mutableStateOf("Player 1")
    val playerTwoName = mutableStateOf("Player 2")

    fun selectGameType(type: String) {
        when (type) {
            "301" -> {
                is301Selected.value = true
                is501Selected.value = false
            }
            "501" -> {
                is301Selected.value = false
                is501Selected.value = true
            }
        }
    }

    fun selectBestOf(type: String) {
        when (type) {
            "Bo1" -> {
                isBo1Selected.value = true
                isBo3Selected.value = false
                isBo5Selected.value = false
            }
            "Bo3" -> {
                isBo1Selected.value = false
                isBo3Selected.value = true
                isBo5Selected.value = false
            }
            "Bo5" -> {
                isBo1Selected.value = false
                isBo3Selected.value = false
                isBo5Selected.value = true
            }
        }
    }

    fun selectPlayers(type: String) {
        when (type) {
            "1 Player" -> {
                isPlayerOneSelected.value = true
                isPlayerTwoSelected.value = false
            }
            "2 Players" -> {
                isPlayerOneSelected.value = false
                isPlayerTwoSelected.value = true
            }
        }
    }

    fun createGame(): MutableMap<String, String> {
        val gameInfo = mutableMapOf<String, String>()
        gameInfo["gameType"] = if (is301Selected.value) "301" else "501"

        gameInfo["bestOf"] = when {
            isBo1Selected.value -> "bestOf1"
            isBo3Selected.value -> "bestOf3"
            isBo5Selected.value -> "bestOf5"
            else -> "bestOf1"
        }

        gameInfo["players"] = if (isPlayerOneSelected.value) "onePlayer" else "twoPlayers"

        gameInfo["playerOneName"] = playerOneName.value
        gameInfo["playerTwoName"] = if (isPlayerTwoSelected.value) playerTwoName.value else ""

        //Log.d("test", "$gameInfo")
        return gameInfo
    }
}