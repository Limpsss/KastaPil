package com.example.kastapil.viewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InGameViewModel : ViewModel() {

    public val playerOneCurrScore = mutableStateOf(0)
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

    // Score = Dart score, type = single, double or triple
    fun calculateScore(index: Int, type: String) {
        if (type == "OuterBullseye") {
            playerOneCurrScore.value += 25
        }
        else if (type == "InnerBullseye") {
            playerOneCurrScore.value += 50
        }
        else {
            val points = dartboardMapping[index]
            val multi = mapHitType[type]

            // reset points for testing purpose
            if (points == 1) {
                playerOneCurrScore.value = 0
                return
            }

            if (points != null && multi != null) {
                playerOneCurrScore.value += points * multi
            }
        }
    }

    fun onSegmentClicked(segmentIndex: Int) {
        println("Segment $segmentIndex clicked")
    }
}