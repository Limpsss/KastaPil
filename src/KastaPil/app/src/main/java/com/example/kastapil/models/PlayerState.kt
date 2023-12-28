package com.example.kastapil.models

import androidx.compose.runtime.*

class PlayerState(name: String, initialScore: Int) {
    var name by mutableStateOf(name)
    val initialScore = initialScore
    var currScore by mutableStateOf(initialScore)
    var dartCount = 0
    var roundsUntilSingleOut by mutableStateOf(5)
    var onSingleOut = false
    var legsWon by mutableStateOf(0)
    var dart1 by mutableStateOf("")
    var dart2 by mutableStateOf("")
    var dart3 by mutableStateOf("")


    fun updateScore(points: Int, dartText: String) {
        dartCount += 1
        currScore -= points

        when (dartCount) {
            1 -> dart1 = dartText
            2 -> dart2 = dartText
            3 -> dart3 = dartText
        }
    }

    fun endTurn() {
        // reset values when players turn ends
        dartCount = 0
        onSingleOut = false
        dart1 = ""
        dart2 = ""
        dart3 = ""
    }

    fun endLeg(won: Boolean) {
        endTurn()
        currScore = initialScore
        if (won) {
            legsWon += 1
        }
    }
}