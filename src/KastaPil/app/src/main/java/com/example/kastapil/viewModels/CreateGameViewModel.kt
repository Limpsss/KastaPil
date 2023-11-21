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

    val playerOneName = mutableStateOf("Limps")
    val playerTwoName = mutableStateOf("")

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

    fun onNameOneChanged(newString: String) {
        playerOneName.value = newString
    }

    fun onNameTwoChanged(newString: String) {
        playerTwoName.value = newString
    }

    // Validating the player names
    fun validateNames(): Boolean {
        if (isPlayerTwoSelected.value) {
            if (playerOneName.value.isNotEmpty() &&
                playerOneName.value.length <= 10 &&
                playerTwoName.value.isNotEmpty() &&
                playerTwoName.value.length <= 10)
                return true
        }
        else {
            if (playerOneName.value.isNotEmpty() &&
                playerOneName.value.length <= 10)
                return true
        }
        return false
    }

    fun createGame(): MutableMap<String, String> {
        val gameInfo = mutableMapOf<String, String>()
        gameInfo["gameType"] = when {
            is301Selected.value -> "301"
            is501Selected.value -> "501"
            else -> "ERROR"
        }

        gameInfo["bestOf"] = when {
            isBo1Selected.value -> "bestOf1"
            isBo3Selected.value -> "bestOf3"
            isBo5Selected.value -> "bestOf5"
            else -> "ERROR"
        }

        gameInfo["playerAmount"] = when {
            isPlayerOneSelected.value -> "onePlayer"
            isPlayerTwoSelected.value -> "twoPlayers"
            else -> "ERROR"
        }

        gameInfo["playerOneName"] = playerOneName.value
        gameInfo["playerTwoName"] = playerTwoName.value

        //Log.d("test", "$gameInfo")
        return gameInfo
    }
}