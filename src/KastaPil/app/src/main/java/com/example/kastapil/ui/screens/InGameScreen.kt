@file:Suppress("NAME_SHADOWING")

package com.example.kastapil.ui.screens

import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kastapil.R
import com.example.kastapil.viewModels.InGameViewModel
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.pow
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

    // on touch arc
    var touchedArcIndex by remember { mutableStateOf(-1.0) }
    val numberOfArcs = 20
    val arcColors = List(numberOfArcs) { index ->
        if (index == touchedArcIndex.toInt()) Color.Red else Color.Blue
    }

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
                        Log.d("test", "SIZE: x: ${size.width}, y: ${size.height}")
                        val center = Offset(size.width/2f, size.height/2f)

                        // Calculate Click Coordinates Relative to the Center
                        val x_rel = touchPoint.x - center.x
                        val y_rel = center.y - touchPoint.y

                        // Calculate the Angle of the Click
                        var theta = (atan2(y_rel, x_rel)).toDouble()

                        // Adjust the Angle for Negative Values
                        if (theta < 0) {
                            theta += 2 * PI
                        }


                        // Determine which sector is pressed
                        val sector_angle = 360 / 20
                        touchedArcIndex = ceil(Math.toDegrees(theta) / sector_angle)



                        Log.d("test", "x: ${touchPoint.x}, y: ${touchPoint.y}")
                        Log.d("test", "THETA: ${theta}")
                        Log.d("test", "Sector: ${touchedArcIndex}")






                        // Initial touch point, start zooming
//                        scale.value = 5f

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
                        // Update the offset as the finger moves
                        val newOffset = canvasOffset.value - dragAmount * 5f
                        canvasOffset.value = newOffset
                    },
                    onDragEnd = {
                        // Reset scale and offset upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                    }
                )
            }
    )

    {
        drawCircle(color = Color.Black)

        val width = size.width
        val height = size.height
        val radius = minOf(width, height) / 2
        val topLeft = Offset((width - 2 * radius) / 2, (height - 2 * radius) / 2)
//        val size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)


        var segmentColor: Color
        for (i in 0..19) {
            if (i % 2 == 0) {
                segmentColor = colorOne
            } else {
                segmentColor = colorTwo
            }

            // White and black color segments
            drawArc(
                color = arcColors[i],
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
                        val center = Offset(size.width/2f, size.height/2f)

                        // Calculate the offset to center the zoom on the touch point
                        val deltaX = (touchPoint.x - center.x) * (1 - scale.value)
                        val deltaY = (touchPoint.y - center.y) * (1 - scale.value)
                        canvasOffset.value = Offset(deltaX, deltaY)

                        tryAwaitRelease()
                        // Reset scale and offset upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                    }
                )
            }
    )

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
