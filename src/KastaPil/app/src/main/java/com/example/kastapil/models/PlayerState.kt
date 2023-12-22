package com.example.kastapil.models

import androidx.compose.runtime.*

class PlayerState(name: String, initialScore: Int) {
    var name by mutableStateOf(name)
    var currScore by mutableStateOf(initialScore)
    var dartCount = 0
    var roundsUntilSingleOut by mutableStateOf(5)
    var onSingleOut = false

    fun updateScore(newScore: Int) {
        currScore = newScore
    }
}