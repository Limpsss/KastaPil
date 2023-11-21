package com.example.kastapil.ui.screens

import android.util.Log
import android.util.Size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
                    .weight(0.6f) // 50% of the screen
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


            var boardSize = remember { mutableStateOf(IntSize(0,0)) }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(color = Color.Red)) {

                // Outer black ring with number
                Canvas(
                    modifier = Modifier.matchParentSize()) {
                        drawCircle(color = Color.Black)
                }

                
                DartSegments(modifier = Modifier
                    .align(Alignment.Center)
                    .size(280.dp))


                // Outer bullseye ring
                Canvas(
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center)) {
                    drawCircle(color = Color.Green)
                }

                // Inner bullseye ring
                Canvas(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.Center)) {
                    drawCircle(color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun DartSegments(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier){
        drawCircle(color = Color.Black)

        val width = size.width
        val height = size.height
        val radius = minOf(width, height) / 2
        val topLeft = Offset((width - 2 * radius) / 2, (height - 2 * radius) / 2)
        val size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)

        // Number 20 segment (from -9° to +9°)
        val startAngle = -99f
        // sweepAngle - size of arch in degrees, each slice is 360°/20 = 18°
        val sweepAngle = 18f

        var segmentColor: Color
        for (i in 0..19) {
            if (i % 2 == 0) {
                segmentColor = Color.Black
            } else {
                segmentColor = Color.White
            }

            drawArc(
                color = segmentColor,
                startAngle = startAngle + i * 18,
                sweepAngle = sweepAngle,
                topLeft = topLeft,
                size = size,
                useCenter = true
            )
        }
    }
}

@Preview
@Composable
fun InGameScreenPreview() {
    val mockGameInfo = mapOf("score" to 10, "player" to "Player1") // Replace with relevant data
    val mockNavController = rememberNavController() // Simple NavController for preview
    val mockViewModel = InGameViewModel() // You will need to create this mock class

    InGameScreen(mockGameInfo, mockNavController, mockViewModel)
}
