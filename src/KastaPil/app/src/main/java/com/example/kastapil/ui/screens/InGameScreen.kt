@file:Suppress("NAME_SHADOWING")

package com.example.kastapil.ui.screens

import android.util.Log
import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kastapil.R
import com.example.kastapil.models.GameInfo
import com.example.kastapil.navigation.Screen
import com.example.kastapil.viewModels.InGameViewModel
import com.example.kastapil.viewModels.InGameViewModelFactory
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun InGameScreen(navController: NavController, viewModel: InGameViewModel) {
    var openAlertDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(Color.Black)
//              .border(BorderStroke(1.dp, Color.Red))

        ){
            val currentPlayerScore = viewModel.currentPlayer.currScore
            val currentPlayerName = viewModel.currentPlayer.name
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Text(
                    text = currentPlayerName,
                    color = Color.White,
                    fontSize = 32.sp,
                )
                Text(
                    text = "$currentPlayerScore",
                    color = Color.White,
                    fontSize = 32.sp
                )
                Row(
                    modifier = Modifier
                ) {
                    Text(
                        text = viewModel.currentPlayer.dart1,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(0.dp,0.dp,5.dp,0.dp))
                    Text(
                        text = viewModel.currentPlayer.dart2,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(0.dp,0.dp,5.dp,0.dp))
                    Text(
                        text = viewModel.currentPlayer.dart3,
                    color = Color.White
                    )
                }
            }
        }
        Box (
            modifier = Modifier
                .weight(1f)
                .clip(RectangleShape)
                .fillMaxSize()
                .border(BorderStroke(1.dp, Color.DarkGray))
                .background(color = Color.Black)
        ) {
            val scale = remember { mutableStateOf(1f) }
            val canvasOffset = remember { mutableStateOf(Offset.Zero) }

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            DartBoardBackgroundImage(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(screenWidth * 0.93f, screenWidth * 0.93f),
                scale = scale,
                offset = canvasOffset
            )

            Dartboard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                scale,
                canvasOffset,
                viewModel
            )
        }

        Box(
            modifier = Modifier
                .weight(0.42f)
                .fillMaxSize()
//                .border(BorderStroke(1.dp, Color.Blue))
                .background(color = Color.Black)
        ){
            DomdLogo(modifier = Modifier.fillMaxSize())
        }
    }


    BackHandler(enabled = true) {
        openAlertDialog = true
    }

    if (openAlertDialog) {
        QuitGameDialog(
            onDismissRequest = {
                openAlertDialog = false
           },
            onConfirmation = {
                openAlertDialog = false
                handleBackPressInGameScreen(navController)
           }
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Dartboard(
    modifier: Modifier,
    scale: MutableState<Float>,
    canvasOffset: MutableState<Offset>,
    viewModel: InGameViewModel) {

    var toDraw by remember { mutableStateOf(false) }
    var touchIndex by remember { mutableStateOf(-1.0) }
    var touchRing by remember { mutableStateOf("") }
    var currentTouchPoint by remember { mutableStateOf(Offset.Zero) }
    var initialTouchPoint by remember { mutableStateOf(Offset.Zero) }
    var dragThreshold by remember { mutableStateOf(0) }

    var center by remember { mutableStateOf(Offset.Zero) }
    // Size of each ring in dartboard

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var doublePointRing by remember { mutableStateOf(Size.Zero) }
    var outerSinglePointRing by remember { mutableStateOf(Size.Zero) }
    var triplePointRing by remember { mutableStateOf(Size.Zero) }
    var innerSinglePointRing by remember { mutableStateOf(Size.Zero) }
    var outerBullseye by remember { mutableStateOf(Size.Zero) }
    var innerBullseye by remember { mutableStateOf(Size.Zero) }

    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                canvasSize = it.size
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = canvasOffset.value.x,
                translationY = canvasOffset.value.y
            )
            .pointerInteropFilter { motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("ingamescreen", "TOUCH: ${Offset(motionEvent.x, motionEvent.y)}")
                        // Logic for tap gesture start
                        initialTouchPoint = Offset(motionEvent.x, motionEvent.y)
                        currentTouchPoint = Offset(motionEvent.x, motionEvent.y)
                        scale.value = 6f

                        touchIndex = highlight(
                            center,
                            initialTouchPoint,
                            doublePointRing.width / 2f
                        )
                        touchRing = pointRing(
                            initialTouchPoint,
                            center,
                            doublePointRing.width / 2f,
                            outerSinglePointRing.width / 2f,
                            triplePointRing.width / 2f,
                            innerSinglePointRing.width / 2f,
                            outerBullseye.width / 2f,
                            innerBullseye.width / 2f
                        )

                        // Calculate the initial offset to center the zoom on the touch point
                        val deltaX = (initialTouchPoint.x - center.x) * (1 - scale.value)
                        val deltaY = (initialTouchPoint.y - center.y) * (1 - scale.value)
                        canvasOffset.value = Offset(deltaX, deltaY)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        dragThreshold += 1
                        if (dragThreshold > 10) {
                            // Logic for drag gesture
                            currentTouchPoint =
                                Offset(motionEvent.x / scale.value, motionEvent.y / scale.value)
                            // Highlight the part on the dartboard where user is touching
                            touchIndex = highlight(
                                center,
                                currentTouchPoint,
                                doublePointRing.width / 2f
                            )
                            touchRing = pointRing(
                                currentTouchPoint,
                                center,
                                doublePointRing.width / 2f,
                                outerSinglePointRing.width / 2f,
                                triplePointRing.width / 2f,
                                innerSinglePointRing.width / 2f,
                                outerBullseye.width / 2f,
                                innerBullseye.width / 2f
                            )
                            // Update the offset as the finger moves
                            val dragAmount = currentTouchPoint - initialTouchPoint
                            val newOffset = canvasOffset.value - dragAmount * 3f
                            canvasOffset.value = newOffset
                            initialTouchPoint = currentTouchPoint
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        // Reset scale, offset and highlighted sector upon release
                        scale.value = 1f
                        canvasOffset.value = Offset.Zero
                        viewModel.throwDart(touchIndex.toInt(), touchRing, currentTouchPoint)
                        touchIndex = -1.0
                        touchRing = ""
                        dragThreshold = 0
                        toDraw = true
                    }
                }
                true // Return true to consume the event
            }
    )
    {
        // Initialize the size of each ring in dartboard
        doublePointRing = Size(canvasSize.width * 0.7f, canvasSize.width * 0.7f)
        outerSinglePointRing = Size(canvasSize.width * 0.665f, canvasSize.width * 0.665f)
        triplePointRing = Size(canvasSize.width * 0.42f, canvasSize.width * 0.42f)
        innerSinglePointRing = Size(canvasSize.width * 0.385f, canvasSize.width * 0.385f)
        outerBullseye = Size(canvasSize.width * 0.06f, canvasSize.width * 0.6f)
        innerBullseye = Size(canvasSize.width * 0.025f, canvasSize.width * 0.025f)
        // Center of canvas
        center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)

        drawDartboard(
            center,
            doublePointRing,
            outerSinglePointRing,
            triplePointRing,
            innerSinglePointRing,
            outerBullseye,
            innerBullseye,
            touchIndex,
            touchRing
        )

        drawDarts(scale.value, viewModel)

    }

}

fun DrawScope.drawDartboard(
    center: Offset,
    doublePointRing: Size,
    outerSinglePointRing: Size,
    triplePointRing: Size,
    innerSinglePointRing: Size,
    outerBullseye: Size,
    innerBullseye: Size,
    touchIndex: Double,
    touchRing: String
) {
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
                colors = arrayOf(Color(0xFF00a000), Color.Red)
            }
            1 -> {
                // Outer single point ring
                currSize = outerSinglePointRing
                currRing = "OuterSingle"
                colors = arrayOf(Color(0xFFe7e4c7), Color.Black)
            }
            2 -> {
                // Triple point ring
                currSize = triplePointRing
                currRing = "Triple"
                colors = arrayOf(Color(0xFF00a000), Color.Red)
            }
            3 -> {
                // Inner single point ring
                currSize = innerSinglePointRing
                currRing = "InnerSingle"
                colors = arrayOf(Color(0xFFe7e4c7), Color.Black)
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

fun DrawScope.drawDarts(scale: Float, viewModel: InGameViewModel) {
    val crossSize = 3.dp.toPx()
    val thickness = 4f
    val borderSize = thickness * 1.5f

    for (point in viewModel.drawingDarts) {
        // Draw border lines (background)
        drawLine(
            start = Offset(point.x - crossSize, point.y - crossSize),
            end = Offset(point.x + crossSize, point.y + crossSize),
            color = Color.Black, // Border color
            strokeWidth = borderSize
        )
        drawLine(
            start = Offset(point.x + crossSize, point.y - crossSize),
            end = Offset(point.x - crossSize, point.y + crossSize),
            color = Color.Black, // Border color
            strokeWidth = borderSize
        )

        drawLine(
            start = Offset(point.x - crossSize, point.y - crossSize),
            end = Offset(point.x + crossSize, point.y + crossSize),
            color = Color.Yellow,
            strokeWidth = thickness
        )
        drawLine(
            start = Offset(point.x + crossSize, point.y - crossSize),
            end = Offset(point.x - crossSize, point.y + crossSize),
            color = Color.Yellow,
            strokeWidth = thickness
        )
    }
}

@Composable
fun DartBoardBackgroundImage(
    modifier: Modifier,
    scale: MutableState<Float>,
    offset: MutableState<Offset>
) {
    Canvas(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )

    ) {
        drawCircle(
            color = Color.White,
            style = Stroke(width = 4f)
        )
    }
    Image(
        painter = painterResource(id = R.drawable.dart2),
        contentDescription = "dartboard",
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )
    )
}

@Composable
fun DomdLogo(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logga),
        contentDescription = "Dömd logga",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

fun handleBackPressInGameScreen(navController: NavController) {
    navController.navigate(Screen.Home.route) {
        popUpTo(Screen.Home.route) {
            inclusive = true
        }
    }
}

@Composable
fun QuitGameDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Icon")
        },
        title = {
            Text(text = "Quit game")
        },
        text = {
            Text(text = "Do you want to quit the game? All progress will be lost.")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

fun highlight(center: Offset, touchPoint: Offset, dartboardRadius: Float): Double {
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
    if (distance > dartboardRadius) {
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
    val mockNavController = rememberNavController() // Simple NavController for preview
    val mockViewModel: InGameViewModel =
        viewModel(factory = InGameViewModelFactory(GameInfo(1, 1, 1, "", ""))) // pass param like this
    InGameScreen(mockNavController, mockViewModel)
}
