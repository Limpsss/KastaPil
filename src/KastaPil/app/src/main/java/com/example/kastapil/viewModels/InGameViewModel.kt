package com.example.kastapil.viewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InGameViewModel : ViewModel() {

    val playerOneCurrScore = mutableIntStateOf(0)

    val dartboardMapping = mapOf<Int, Int>(
        0 to 20,
        1 to 1,
        2 to 18,
        3 to 4,
        4 to 13,
        5 to 6,
        6 to 10,
        7 to 15,
        8 to 2,
        9 to 17,
        10 to 3,
        11 to 19,
        12 to 7,
        13 to 16,
        14 to 8,
        15 to 11,
        16 to 14,
        17 to 9,
        18 to 12,
        19 to 5
    )

    // Score = Dart score, type = single, double or triple
    fun addScore(index: Int, type: Int) {
        val scoreValue = dartboardMapping[index]
        if (scoreValue != null) {
            playerOneCurrScore.value += scoreValue * type
        }
    }

    fun onSegmentClicked(segmentIndex: Int) {
        println("Segment $segmentIndex clicked")
    }
}