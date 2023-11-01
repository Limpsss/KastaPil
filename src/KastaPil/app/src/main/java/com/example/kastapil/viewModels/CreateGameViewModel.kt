package com.example.kastapil.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CreateGameViewModel : ViewModel() {
    val gameInfo = mutableStateOf("map med gametype, best of, player names å liknande")

    val is301Selected = mutableStateOf(true)
    val is501Selected = mutableStateOf(false)

    val isBo1Selected = mutableStateOf(true)
    val isBo3Selected = mutableStateOf(false)
    val isBo5Selected = mutableStateOf(false)

    val isPlayerOneSelected = mutableStateOf(true)
    val isPlayerTwoSelected = mutableStateOf(false)

    val twoPlayers = mutableStateOf(false)
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
}