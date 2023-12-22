package com.example.kastapil.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kastapil.models.GameInfo

class InGameViewModelFactory(private val gameInfo: GameInfo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = InGameViewModel(gameInfo) as T
}

class InGameViewModel(public val gameInfo: GameInfo) : ViewModel() {
    // gameInfo =
    //          "gameType"
    //          "bestOf"
    //          "playerAmount"
    //          "playerOneName"
    //          "playerTwoName"

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

    private val startScore: Int
    private val onSingleOut: Boolean = false

    var roundsBeforeSingleOut by mutableStateOf(-1)
    var playerOneCurrScore by mutableStateOf(-1)
    var playerTwoCurrScore by mutableStateOf(-1)

    init {
        // set start score based on gameInfo
        startScore = gameInfo.gameType
        playerOneCurrScore = startScore
        playerTwoCurrScore = startScore
    }

    // Score = Dart score, type = single, double or triple
    fun calculateScore(index: Int, type: String) {
        if (type == "OuterBullseye") {
            playerOneCurrScore -= 25
        }
        else if (type == "InnerBullseye") {
            playerOneCurrScore -= 50
        }
        else {
            val points = dartboardMapping[index]
            val multi = mapHitType[type]

            // reset points for testing purpose
            if (points == 1) {
                playerOneCurrScore = 0
                return
            }

            if (points != null && multi != null) {
                playerOneCurrScore -= points * multi
            }
        }
    }

    fun checkNewScore(prevScore: Int, hitScore: Int): Boolean {
        if (prevScore - hitScore < 0) {
            return false
        }
        if (prevScore - hitScore < 50) {
            // börja räkna ner varje runda från 5, efter 5 miss av dubbel så kan man gå ut på singel
        }
        return true
    }
}