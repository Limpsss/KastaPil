package com.example.kastapil.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kastapil.models.GameInfo

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

    fun selectGameType(type: Int) {
        when (type) {
            301 -> {
                is301Selected.value = true
                is501Selected.value = false
            }
            501 -> {
                is301Selected.value = false
                is501Selected.value = true
            }
        }
    }

    fun selectBestOf(type: Int) {
        when (type) {
            1 -> {
                isBo1Selected.value = true
                isBo3Selected.value = false
                isBo5Selected.value = false
            }
            3 -> {
                isBo1Selected.value = false
                isBo3Selected.value = true
                isBo5Selected.value = false
            }
            5 -> {
                isBo1Selected.value = false
                isBo3Selected.value = false
                isBo5Selected.value = true
            }
        }
    }

    fun selectPlayers(type: Int) {
        when (type) {
            1 -> {
                isPlayerOneSelected.value = true
                isPlayerTwoSelected.value = false
            }
            2 -> {
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

    fun createGame(): GameInfo {
        var gameType = 0
        var bestOf = 0
        var playerAmount = 0
        var playerOne = ""
        var playerTwo = ""

        gameType = when {
            is301Selected.value -> 301
            is501Selected.value -> 501
            else -> -1
        }

        bestOf = when {
            isBo1Selected.value -> 1
            isBo3Selected.value -> 3
            isBo5Selected.value -> 5
            else -> -1
        }

        playerAmount = when {
            isPlayerOneSelected.value -> 1
            isPlayerTwoSelected.value -> 2
            else -> -1
        }

        playerOne = playerOneName.value
        playerTwo = playerTwoName.value

        return GameInfo(
            gameType,
            bestOf,
            playerAmount,
            playerOne,
            playerTwo
        )
    }
}