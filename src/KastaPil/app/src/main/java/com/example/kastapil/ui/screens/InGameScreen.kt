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

    // on touch states
//    var touchedArcIndex by remember { mutableStateOf(-1.0) }
    var touchIndex by remember { mutableStateOf(-1.0) }
    var touchRing by remember { mutableStateOf("") }

    var touchCoordinates by remember { mutableStateOf(Offset.Unspecified) }
    var isDragging by remember { mutableStateOf(false) }

    var center by remember { mutableStateOf(Offset.Zero) }
    // Size of each ring in dartboard
    var doublePointRing by remember { mutableStateOf(Size.Zero) }
    var outerSinglePointRing by remember { mutableStateOf(Size.Zero) }
    var triplePointRing by remember { mutableStateOf(Size.Zero) }
    var innerSinglePointRing by remember { mutableStateOf(Size.Zero) }
    var outerBullseye by remember { mutableStateOf(Size.Zero) }
    var innerBullseye by remember { mutableStateOf(Size.Zero) }

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
                        // Initial touch point, start zooming
                        scale.value = 5f

                        touchIndex = highlight(center, touchPoint)
                        touchRing = pointRing(
                            touchPoint,
                            center,
                            doublePointRing.width/2f,
                            outerSinglePointRing.width/2f,
                            triplePointRing.width/2f,
                            innerSinglePointRing.width/2f,
                            outerBullseye.width/2f,
                            innerBullseye.width/2f)

                        // Calculate the initial offset to center the zoom on the touch point
                        val deltaX = (touchPoint.x - center.x) * (1 - scale.value)
                        val deltaY = (touchPoint.y - center.y) * (1 - scale.value)
                        canvasOffset.value = Offset(deltaX, deltaY)

                        tryAwaitRelease()
                        if (!isDragging) {
                            // Reset on release if there was no drag
                            scale.value = 1f
                            canvasOffset.value = Offset.Zero
                            touchIndex = -1.0
                            touchRing = ""
                        }
                        isDragging = false // Reset drag state
                    },
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { touch, dragAmount ->

                        // Update drag state
                        isDragging = true
                        // Update touch coordinates
                        touchCoordinates = touch.position
                        // Highlight the part on the dartboard where user is touching

                        touchIndex = highlight(center, touchCoordinates)
                        touchRing = pointRing(
                            touchCoordinates,
                            center,
                            doublePointRing.width/2f,
                            outerSinglePointRing.width/2f,
                            triplePointRing.width/2f,
                            innerSinglePointRing.width/2f,
                            outerBullseye.width/2f,
                            innerBullseye.width/2f)

                        Log.d("testtest", "ring: ${touchRing}, index: ${touchIndex}")

                        // Update the offset as the finger moves
                        val newOffset = canvasOffset.value - dragAmount * 5f
                        canvasOffset.value = newOffset
                    },
                    onDragEnd = {

                        // Reset scale, offset and highlighted sector upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                        touchIndex = -1.0
                        touchRing = ""
                        isDragging = false // Reset drag state
                    }
                )
            }
    )

    {
        // Initialize the size of each ring in dartboard
        doublePointRing = size
        outerSinglePointRing = Size(765f, 765f)
        triplePointRing = Size(495f, 495f)
        innerSinglePointRing = Size(450f, 450f)
        outerBullseye = Size(75f, 75f)
        innerBullseye = Size(30f, 30f)

        // Center of canvas
        center = Offset(size.width / 2f, size.height / 2f)

        val startAngle = -9f
        // sweepAngle - size of arch in degrees, each slice is 360°/20 = 18°
        val sweepAngle = 18f

        val sectorColor = mutableMapOf<String, Color>()
        var colors = arrayOf(Color.White)
        var currRing = ""
        var currSize = size

        // Drawing circles to display dartboard
        for (j in 0..3) {
            when (j) {
                0 -> {
                    // Double point ring
                    currSize = doublePointRing
                    currRing = "Double"
                    colors = arrayOf(Color.Red, Color(0xFF00a000))
                }
                1 -> {
                    // Outer single point ring
                    currSize = outerSinglePointRing
                    currRing = "OuterSingle"
                    colors = arrayOf(Color.Black, Color(0xFFe7e4c7))
                }
                2 -> {
                    // Triple point ring
                    currSize = triplePointRing
                    currRing = "Triple"
                    colors = arrayOf(Color.Red, Color(0xFF00a000))
                }
                3 -> {
                    // Inner single point ring
                    currSize = innerSinglePointRing
                    currRing = "InnerSingle"
                    colors = arrayOf(Color.Black, Color(0xFFe7e4c7))
                }
            }
            // Drawing 20 segments to form a circle
            for (i in 0..19) {
                when {
                    // Color for the selected arc
                    ( i == touchIndex.toInt() && currRing == touchRing ) -> sectorColor[touchRing] = Color.Yellow
                    // Regular color scheme
                    ( i % 2 == 0 ) -> sectorColor[currRing] = colors[0]
                    else -> sectorColor[currRing] = colors[1]
                }

                // Segments as circle
                drawArc(
                    color = sectorColor[currRing]!!,
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
                currSize = outerBullseye
                currRing = "OuterBullseye"
                colors = arrayOf(Color(0xFF00a000))
            }
            else {
                currSize = innerBullseye
                currRing = "InnerBullseye"
                colors = arrayOf(Color.Red)
            }

            when {
                // If curr ring is being touched, highlight it
                ( currRing == touchRing ) -> sectorColor[touchRing] = Color.Yellow
                else -> sectorColor[currRing] = colors[0]
            }

                // Bullseye ring:
                drawCircle(
                    radius = currSize.width / 2,
                    color = sectorColor[currRing]!!)

                // White border:
                drawCircle(
                    radius = currSize.width / 2,
                    color = Color.White,
                    style = Stroke(2f))
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
    if (distance > 810 / 2) {
        // If touch is outside of circle return -1
        touchedIndex = -1.0
    } else {
        touchedIndex = index
        if (touchedIndex == 20.0) {
            touchedIndex = 0.0
        }
    }
    return touchedIndex
}

fun pointRing(
    point: Offset,
    origo: Offset,
    radiusDouble: Float,
    radiusOuterSingle: Float,
    radiusTriple: Float,
    radiusInnerSingle: Float,
    radiusOuterBullseye: Float,
    radiusInnerBullseye: Float): String {

    Log.d("test", "COORDINATES: ${point}")

    // sqrt ( (x - x^o)^2 + (y - y^2)^2 )
    val pointOnCircle = sqrt( (point.x - origo.x).pow(2) + (point.y - origo.y).pow(2) )

    when {
        (pointOnCircle < radiusDouble && pointOnCircle > radiusOuterSingle) -> return "Double" // Double ring
        (pointOnCircle < radiusDouble && pointOnCircle > radiusTriple) -> return "OuterSingle" // Outer single ring
        (pointOnCircle < radiusOuterSingle && pointOnCircle > radiusInnerSingle) -> return "Triple" // Triple ring
        (pointOnCircle < radiusTriple && pointOnCircle > radiusOuterBullseye) -> return "InnerSingle" // Inner single ring

        (pointOnCircle < radiusInnerSingle && pointOnCircle > radiusInnerBullseye) -> return "OuterBullseye" // Outer bullseye
        (pointOnCircle < radiusOuterBullseye) -> return "InnerBullseye" // Inner bullseye

        else -> return "Miss"
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
