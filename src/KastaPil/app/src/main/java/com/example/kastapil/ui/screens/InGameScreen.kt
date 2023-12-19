@file:Suppress("NAME_SHADOWING")

package com.example.kastapil.ui.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kastapil.R
import com.example.kastapil.viewModels.InGameViewModel
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.ceil
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
                Dartboard(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(270.dp),
                    scale,
                    canvasOffset,
                    viewModel)
            }
        }
    }
}



@Composable
fun Dartboard(
    modifier: Modifier,
    scale: MutableState<Float>,
    canvasOffset: MutableState<Offset>,
    viewModel: InGameViewModel) {

    // on touch arc
    var touchedArcIndex by remember { mutableStateOf(-1.0) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var touchCoordinates by remember { mutableStateOf(Offset.Unspecified) }

    Canvas(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = canvasOffset.value.x,
                translationY = canvasOffset.value.y
            )
            // Code that works on drag zoom
            .pointerInput(Unit) {
                detectTapGestures(

                    onPress = { touchPoint ->
//                        val center = Offset(size.width / 2f, size.height / 2f)

//                        Log.d("test", "x: ${touchPoint.x}, y: ${touchPoint.y}")
//                        Log.d("test", "THETA: ${theta}")
//                        Log.d("test", "Sector: ${touchedArcIndex.toInt()}")


                        // Initial touch point, start zooming
                        scale.value = 5f

                        // Calculate the initial offset to center the zoom on the touch point
                        val deltaX = (touchPoint.x - center.x) * (1 - scale.value)
                        val deltaY = (touchPoint.y - center.y) * (1 - scale.value)
                        canvasOffset.value = Offset(deltaX, deltaY)
                    },
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
//                        touchCoordinates = change.position

                        // On release highlight the correct part of dartboard
                        touchedArcIndex = highlight(center, change.position)

                        // Update the offset as the finger moves
                        val newOffset = canvasOffset.value - dragAmount * 5f
                        canvasOffset.value = newOffset
                    },
                    onDragEnd = {

                        // Print out last touched sector
                        Log.d("test", "Sector: ${touchedArcIndex.toInt()}")

                        // Reset scale, offset and highlighted sector upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                        touchedArcIndex = -1.0
                    }
                )
            }
    )

    {
        // Center of canvas
        center = Offset(size.width / 2, size.height / 2)

        val startAngle = -9f
        // sweepAngle - size of arch in degrees, each slice is 360°/20 = 18°
        val sweepAngle = 18f

        var sectorColor: Color
        var colors: Array<Color>

        var currSize = size

        // Drawing circles to display dartboard
        for (j in 0..3) {
            when (j) {
                0 -> currSize = size
                1 -> currSize = Size(765f, 765f)
                2 -> currSize = Size(495f, 495f)
                3 -> currSize = Size(450f, 450f)
            }
            // First and third rings should be red and green to highlight double and triple ring
            if (j % 2 == 0) {
                colors = arrayOf(Color.Red, Color(0xFF00a000))
            }
            // Second and fourth ring should be black and white to highlight single point rings
            else {
                colors = arrayOf(Color.Black, Color(0xFFe7e4c7))
            }

            // Drawing 20 segments to form a circle
            for (i in 0..19) {
                when {
                    // Color for the selected arc
                    ( i == touchedArcIndex.toInt() ) -> sectorColor = Color.Yellow
                    // Regular color scheme
                    ( i % 2 == 0 ) -> sectorColor = colors[1]
                    else -> sectorColor = colors[0]
                }

                // Segments as circle
                drawArc(
                    color = sectorColor,
                    startAngle = startAngle + (-i) * 18,
                    sweepAngle = sweepAngle,
                    topLeft = center - Offset(currSize.width / 2, currSize.height / 2),
                    size = currSize,
                    useCenter = true
                )

                // White borders around segments
                drawArc(
                    color = Color.White,
                    startAngle = startAngle + (-i) * 18,
                    sweepAngle = sweepAngle,
                    topLeft = center - Offset(currSize.width / 2, currSize.height / 2),
                    size = currSize,
                    useCenter = true,
                    style = Stroke(width = 2f)
                )
            }
        }
        // Inner & outer bullseye rings
        for (i in 0..1) {
            if (i == 0) {
                currSize = Size(75f, 75f)
                // Outer bullseye ring:
                drawCircle(
                    radius = currSize.width / 2,
                    color = Color(0xFF00a000))

                // White border:
                drawCircle(
                    radius = currSize.width / 2,
                    color = Color.White,
                    style = Stroke(2f))
            }
            else {
                currSize = Size(30f, 30f)
                // Inner bullseye ring:
                drawCircle(
                    radius = currSize.width / 2,
                    color = Color.Red)
                // White border:
                drawCircle(
                    radius = currSize.width / 2,
                    color = Color.White,
                    style = Stroke(2f))
            }
        }
    }
}

fun highlight(center: Offset, touchPoint: Offset): Double {
    var touchedIndex: Double
    // Calculate click coordinates relative to the center
    val xRel = touchPoint.x - center.x
    val yRel = center.y - touchPoint.y

    // Calculate the angle of the click
    var theta = (atan2(yRel, xRel)).toDouble()

    // Adjust the angle for negative values
    if (theta < 0) {
        theta += 2 * PI
    }

    // Determine which sector is pressed
    val sectorAngle = 360 / 20
    val index = ceil((Math.toDegrees(theta) - 9) / sectorAngle)


    // Check if the click is inside the circle
    val distance = sqrt(xRel * xRel + yRel * yRel)
    if (distance > 743 / 2) {
        // If touch is outside of circle return -1
        touchedIndex = -1.0
    } else {
        touchedIndex = index
        
    }

    return touchedIndex
}

// old dartsegment composable

//@Composable
//fun DartSegments(
//    modifier: Modifier = Modifier,
//    colorOne: Color,
//    colorTwo: Color,
//    scale: MutableState<Float>,
//    canvasOffset: MutableState<Offset>,
//    viewModel: InGameViewModel) {
//    val startAngle = -99f
//    // sweepAngle - size of arch in degrees, each slice is 360°/20 = 18°
//    val sweepAngle = 18f
//
//    // on touch arc
//    var touchedArcIndex by remember { mutableStateOf(-1.0) }
//
//
//    Canvas(
//        modifier = modifier
//            .graphicsLayer(
//                scaleX = scale.value,
//                scaleY = scale.value,
//                translationX = canvasOffset.value.x,
//                translationY = canvasOffset.value.y
//            )
//            // Code that works on drag zoom
//            .pointerInput(Unit) {
//                detectTapGestures(
//
//                    onPress = { touchPoint ->
//                        Log.d("test", "SIZE: x: ${size.width}, y: ${size.height}")
//                        val center = Offset(size.width / 2f, size.height / 2f)
//
//                        // Calculate click coordinates relative to the center
//                        val x_rel = touchPoint.x - center.x
//                        val y_rel = center.y - touchPoint.y
//
//                        // Calculate the angle of the click
//                        var theta = (atan2(y_rel, x_rel)).toDouble()
//
//                        // Adjust the angle for negative values
//                        if (theta < 0) {
//                            theta += 2 * PI
//                        }
//
//                        // Determine which sector is pressed
//                        val sector_angle = 360 / 20
//                        val index = ceil((Math.toDegrees(theta) - 9) / sector_angle)
//
//
//                        // Check if the click is inside the circle
//                        val distance = sqrt(x_rel * x_rel + y_rel * y_rel)
//                        if (distance > 743 / 2) {
//                            Log.d("test", "CLICK IS OUTSIDE OF CIRCLE")
//                            touchedArcIndex = -1.0
//                        } else {
//                            touchedArcIndex = index
//                            if (touchedArcIndex == 20.0) {
//                                touchedArcIndex = 0.0
//                            }
//                        }
//
//
//                        Log.d("test", "x: ${touchPoint.x}, y: ${touchPoint.y}")
//                        Log.d("test", "THETA: ${theta}")
//                        Log.d("test", "Sector: ${touchedArcIndex.toInt()}")
//
//
//                        // Initial touch point, start zooming
////                        scale.value = 5f
//
//                        // Calculate the initial offset to center the zoom on the touch point
//                        val deltaX = (touchPoint.x - center.x) * (1 - scale.value)
//                        val deltaY = (touchPoint.y - center.y) * (1 - scale.value)
//                        canvasOffset.value = Offset(deltaX, deltaY)
//                    },
//                )
//            }
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDrag = { change, dragAmount ->
//                        // Update the offset as the finger moves
//                        val newOffset = canvasOffset.value - dragAmount * 5f
//                        canvasOffset.value = newOffset
//                    },
//                    onDragEnd = {
//                        // Reset scale and offset upon release
//                        scale.value = 1f
//                        canvasOffset.value = Offset.Zero
//                    }
//                )
//            }
//    )
//
//    {
//        drawCircle(color = Color.Black)
//
//        val width = size.width
//        val height = size.height
//        val radius = minOf(width, height) / 2
//        val topLeft = Offset((width - 2 * radius) / 2, (height - 2 * radius) / 2)
//
//
//
//        var segmentColor: Color
//        for (i in 0..19) {
//            when {
//                i == touchedArcIndex.toInt() -> segmentColor = Color.Yellow // Color for the selected arc
//                i % 2 == 0 -> segmentColor = colorOne // Regular color scheme
//                else -> segmentColor = colorTwo
//            }
//
//
//            // White and black color segments
//            drawArc(
//                color = segmentColor,
//                startAngle = startAngle + i * 18,
//                sweepAngle = sweepAngle,
//                topLeft = topLeft,
//                size = size,
//                useCenter = true
//            )
//
//            // White borders around segments
//            drawArc(
//                color = Color.White,
//                startAngle = startAngle + i * 18,
//                sweepAngle = sweepAngle,
//                topLeft = topLeft,
//                size = size,
//                useCenter = true,
//                style = Stroke(width = 2f)
//            )
//        }
//    }
//}


//@Composable
//fun BullseyeRing(modifier: Modifier,
//                 color: Color,
//                 scale: MutableState<Float>,
//                 canvasOffset: MutableState<Offset>,) {
//    Canvas(
//        modifier = modifier
//            .graphicsLayer(
//                scaleX = scale.value,
//                scaleY = scale.value,
//                translationX = canvasOffset.value.x,
//                translationY = canvasOffset.value.y
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = { touchPoint ->
//                        scale.value = 5f
//                        val center = Offset(size.width / 2f, size.height / 2f)
//
//                        // Calculate the offset to center the zoom on the touch point
//                        val deltaX = (touchPoint.x - center.x) * (1 - scale.value)
//                        val deltaY = (touchPoint.y - center.y) * (1 - scale.value)
//                        canvasOffset.value = Offset(deltaX, deltaY)
//
//                        tryAwaitRelease()
//                        // Reset scale and offset upon release
//                        scale.value = 1f
//                        canvasOffset.value = Offset.Zero
//                    }
//                )
//            }
//    )
//
//    {
//        drawCircle(color = color)
//
//        // White border:
//        drawCircle(
//            color = Color.White,
//            style = Stroke(2f))
//    }
//}

@Preview
@Composable
fun InGameScreenPreview() {
    val mockGameInfo = mapOf("score" to 10, "player" to "Player1") // Replace with relevant data
    val mockNavController = rememberNavController() // Simple NavController for preview
    val mockViewModel = InGameViewModel() // You will need to create this mock class

    InGameScreen(mockGameInfo, mockNavController, mockViewModel)
}
