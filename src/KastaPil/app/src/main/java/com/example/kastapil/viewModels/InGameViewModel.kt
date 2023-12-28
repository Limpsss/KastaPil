package com.example.kastapil.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kastapil.models.GameInfo
import com.example.kastapil.models.PlayerState

class InGameViewModelFactory(private val gameInfo: GameInfo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = InGameViewModel(gameInfo) as T
}

class InGameViewModel(public val gameInfo: GameInfo) : ViewModel() {

    private val dartboardMapping = mapOf<Int, Int>(
        0 to 6,
        1 to 13,
        2 to 4,
        3 to 18,
        4 to 1,
        5 to 20,
        6 to 5,
        7 to 12,
        8 to 9,
        9 to 14,
        10 to 11,
        11 to 8,
        12 to 16,
        13 to 7,
        14 to 19,
        15 to 3,
        16 to 17,
        17 to 2,
        18 to 15,
        19 to 10
    )
    private val mapHitType = mapOf<String, Int>(
        "OuterSingle" to 1,
        "InnerSingle" to 1,
        "Double" to 2,
        "Triple" to 3
    )

    // Initialize players
    private val players = if (gameInfo.playerAmount == 2) {
        listOf(
            PlayerState(gameInfo.playerOneName, gameInfo.gameType),
            PlayerState(gameInfo.playerTwoName, gameInfo.gameType)
        )
    } else {
        listOf(
            PlayerState(gameInfo.playerOneName, gameInfo.gameType)
        )
    }
    // Observable state for the current player index
    private var currentPlayerIndex = 0
    // Current player as a state
    var currentPlayer by mutableStateOf(players[currentPlayerIndex])

    // Score = Dart score, type = single, double or triple
    fun calculateScore(index: Int, type: String) {
//        Log.d("ingameviewmodel", "player: ${currentPlayer.name}, score: ${currentPlayer.currScore}")
        var points = 0
        if (type == "OuterBullseye") {
            points = 25
        }
        else if (type == "InnerBullseye") {
            points = 50
        }
        else {
            val single = dartboardMapping[index]
            val multi = mapHitType[type]
            if (single != null && multi != null) {
                points = single * multi
            }
        }

        val newScore = currentPlayer.currScore - points
        if (checkValidNewScore(newScore, type)) {
            // update current player score
            updateScore(points, type)
            if (currentPlayer.currScore == 0) {
                // call a win set function
                endLeg()
            }
        }
        // ends turn if 3 darts have been thrown
        endTurn(currentPlayer.currScore, newScore)
    }

    private fun checkValidNewScore(newScore: Int, type: String): Boolean {
        val result: Boolean
        // out on single rules
        if (currentPlayer.onSingleOut) {
            if (newScore == 0) {
                // you won the set!!!
                Log.d("ingameviewmodel", "you won on single out!!!")
                result = true
            }
            else if (newScore < 0) {
                // error message
                Log.d("ingameviewmodel", "Score 1 or below, try again!")
                result = false
            }
            else {
                Log.d("edf", "dsf")
                result =  true
            }
        }
        // regular rules
        else {
            val isDouble = type == "Double" || type == "InnerBullseye"
            if (newScore == 0 && isDouble) {
                // You won the set!!!
                Log.d("ingameviewmodel", "you won on double out")
                result = true
            }
            else if (newScore <= 1) {
                // error message
                Log.d("ingameviewmodel", "Score 1 or below, try again!")
                result = false
            }
            else {
                Log.d("edf", "dsf")
                result = true
            }
        }
        return result
    }

    private fun updateScore(points: Int, type: String) {
        var dartText = ""
        when {
            type == "OuterSingle" || type == "InnerSingle" || type == "OuterBullseye" -> dartText = "S$points"
            type == "Double" || type == "InnerBullseye" -> dartText = "D${points/2}"
            type == "Triple" -> dartText = "T${points/3}"
            else -> dartText = type // Should be "Miss" in this else case
        }
        currentPlayer.updateScore(points, dartText)
    }

    private fun endTurn(prevScore: Int, newScore: Int) {
        // end turn if player has thrown 3 darts
        if (currentPlayer.dartCount == 3) {
            currentPlayer.endTurn()

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size
            currentPlayer = players[currentPlayerIndex]
        }

//        if (!playerOneState.onSingleOut) {
//            if (prevScore >= 50 && newScore < 50) {
//
//            }
//        }
    }

    private fun endLeg() {
        // do some ui stuff

        for(player in players) {
            var won = false
            if (player.currScore == 0) {
                won = true
            } 
            player.endLeg(won)

            val setsWon = player.legsWon
            val bestOf = gameInfo.bestOf
            if ( ( setsWon == 1 && bestOf == 1 ) || ( setsWon == 2 && bestOf == 3 ) || setsWon == 3 ) {
                endGame()
            }
        }
    }

    private fun endGame() {
        Log.d("ingameviewmodel", "YOU WOOOOOON!")
        // some ui stuff or whatever
    }
}