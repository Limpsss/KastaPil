package com.example.kastapil.models

import androidx.compose.runtime.*

class PlayerState(name: String, val initialScore: Int, val isAppUser: Boolean) {
    var name by mutableStateOf(name)
    var currScore by mutableStateOf(initialScore)
    var prevScore = initialScore
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

    fun endTurn(isValidRound: Boolean) {
        // reset values when players turn ends
        dartCount = 0
        onSingleOut = false
        dart1 = ""
        dart2 = ""
        dart3 = ""

        // If the round was invalid (score going to 1 or below 0) revert curr score to prev score
        if (!isValidRound) {
            currScore = prevScore
        } else {
            prevScore = currScore
        }
    }

    fun endLeg(won: Boolean) {
        endTurn(true)
        currScore = initialScore
        if (won) {
            legsWon += 1
        }
    }
}