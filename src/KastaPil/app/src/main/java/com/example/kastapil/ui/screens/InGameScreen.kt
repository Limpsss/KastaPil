package com.example.kastapil.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kastapil.navigation.Screen
import com.example.kastapil.viewModels.CreateGameViewModel
import com.example.kastapil.viewModels.InGameViewModel

@Composable
fun InGameScreen(gameInfo: Map<*,*>, navController: NavController, viewModel: InGameViewModel) {
    // gameIfo =
    //          "gameType"
    //          "bestOf"
    //          "playerAmount"
    //          "playerOneName"
    //          "playerTwoName"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)

    ){
        Column(
            modifier = Modifier
                .fillMaxSize())
        {
            // Top half of the screen:
            Column(
                modifier = Modifier
                    .weight(1f) // 50% of the screen
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (gameInfo["playerAmount"] == 1) {
                    Text(
                        text = gameInfo["playerOneName"].toString(),
                        fontSize = 32.sp
                    )
                }
                else {
                    Text(
                        text = gameInfo["playerOneName"].toString(),
                        fontSize = 32.sp
                    )
                    Text(
                        text = gameInfo["playerTwoName"].toString(),
                        fontSize = 32.sp
                    )
                }
            }

            // Bottom half for the dartboard canvas:
            Canvas(modifier = Modifier
                .weight(1f) // 50% of the screen
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        Log.d("test", "IM CLICKING")
                    }
                }
            ) {
                // Dartboard:
                drawCircle(color = Color.Red)
            }
        }

    }
}
