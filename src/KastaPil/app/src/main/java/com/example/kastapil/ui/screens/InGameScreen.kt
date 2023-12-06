@file:Suppress("NAME_SHADOWING")

package com.example.kastapil.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kastapil.R
import com.example.kastapil.viewModels.InGameViewModel
import kotlin.math.atan2
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
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

                Text(
                    text = "Current score: " + viewModel.playerOneCurrScore.value.toString(),
                    fontSize = 32.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(color = Color.DarkGray)) {

                var scale = remember { mutableStateOf(1f) }
                var canvasOffset = remember { mutableStateOf(Offset.Zero) }


                // How i will draw:
//                var drawingPoints by remember { mutableStateOf(listOf<Offset>()) }
//                            }) { offset ->
//                            drawingPoints = drawingPoints + offset
//                        }
//                    })

                Image(
                    painter = painterResource(id = R.drawable.dart2),
                    contentDescription = "dartboard",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .size(355.dp)
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            translationX = canvasOffset.value.x,
                            translationY = canvasOffset.value.y
                        )
                )

                // DOUBLE point ring (Red, Green)
                DartSegments(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(270.dp),
                    colorOne = Color.Red,
                    colorTwo = Color(0xFF00a000),
                    scale,
                    canvasOffset,
                    viewModel)

                // NORMAL point outer ring (Black, White)
                DartSegments(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(255.dp),
                    colorOne = Color.Black,
                    colorTwo = Color(0xFFe7e4c7),
                    scale,
                    canvasOffset,
                    viewModel)

                // TRIPLE point ring (Red, Green)
                DartSegments(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(165.dp),
                    colorOne = Color.Red,
                    colorTwo = Color(0xFF00a000),
                    scale,
                    canvasOffset,
                    viewModel)

                // NORMAL point outer ring (Black, White)
                DartSegments(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(150.dp),
                    colorOne = Color.Black,
                    colorTwo = Color(0xFFe7e4c7),
                    scale,
                    canvasOffset,
                    viewModel)

                // Outer bullseye ring
                BullseyeRing(
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),
                    color = Color(0xFF00a000),
                    scale = scale,
                    canvasOffset = canvasOffset
                )

                // Inner bullseye ring
                BullseyeRing(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.Center),
                    color = Color.Red,
                    scale = scale,
                    canvasOffset = canvasOffset
                )
            }
        }
    }
}

@Composable
fun DartSegments(
    modifier: Modifier = Modifier,
    colorOne: Color,
    colorTwo: Color,
    scale: MutableState<Float>,
    canvasOffset: MutableState<Offset>,
    viewModel: InGameViewModel) {
    val startAngle = -99f
    // sweepAngle - size of arch in degrees, each slice is 360°/20 = 18°
    val sweepAngle = 18f

    Canvas(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = canvasOffset.value.x,
                translationY = canvasOffset.value.y
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { touchPoint ->
                        scale.value = 5f
                        val centerX = size.width / 2
                        val centerY = size.height / 2

                        // Calculate the offset to center the zoom on the touch point
                        val deltaX = (touchPoint.x - centerX) * (1 - scale.value)
                        val deltaY = (touchPoint.y - centerY) * (1 - scale.value)
                        canvasOffset.value = Offset(deltaX, deltaY)

                        tryAwaitRelease()

                        // Reset scale and offset upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                    })
            })
    {
        drawCircle(color = Color.Black)

        val width = size.width
        val height = size.height
        val radius = minOf(width, height) / 2
        val topLeft = Offset((width - 2 * radius) / 2, (height - 2 * radius) / 2)
        val size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)


        var segmentColor: Color
        for (i in 0..19) {
            if (i % 2 == 0) {
                segmentColor = colorOne
            } else {
                segmentColor = colorTwo
            }

            // White and black color segments
            drawArc(
                color = segmentColor,
                startAngle = startAngle + i * 18,
                sweepAngle = sweepAngle,
                topLeft = topLeft,
                size = size,
                useCenter = true
            )

            // White borders around segments
            drawArc(
                color = Color.White,
                startAngle = startAngle + i * 18,
                sweepAngle = sweepAngle,
                topLeft = topLeft,
                size = size,
                useCenter = true,
                style = Stroke(width = 2f)
            )
        }
    }
}

@Composable
fun BullseyeRing(modifier: Modifier,
                 color: Color,
                 scale: MutableState<Float>,
                 canvasOffset: MutableState<Offset>,) {
    Canvas(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = canvasOffset.value.x,
                translationY = canvasOffset.value.y
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { touchPoint ->
                        scale.value = 5f
                        val centerX = size.width / 2
                        val centerY = size.height / 2

                        // Calculate the offset to center the zoom on the touch point
                        val deltaX = (touchPoint.x - centerX) * (1 - scale.value)
                        val deltaY = (touchPoint.y - centerY) * (1 - scale.value)
                        canvasOffset.value = Offset(deltaX, deltaY)

                        tryAwaitRelease()
                        // Reset scale and offset upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                    })
            })
    {
        drawCircle(color = color)

        // White border:
        drawCircle(
            color = Color.White,
            style = Stroke(2f))
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
