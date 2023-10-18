package com.example.kastapil

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CreateGameScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {
        Text(
            modifier = Modifier
                .padding(start = 24.dp, top = 8.dp)
                .clickable {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                },
            text = "x",
            color = Color.White,
            fontSize = 28.sp,

            )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.height(60.dp))
            GameTypeBox()
            Spacer(modifier = Modifier.height(30.dp))
            BestOfBox()
            Spacer(modifier = Modifier.height(30.dp))
            ChoosePlayersBox()
        }
    }
}

@Composable
fun GameTypeBox() {
    val is301Selected = remember { mutableStateOf(true) }
    val is501Selected = remember { mutableStateOf(false) }
    val buttonMap: Map<String, MutableState<Boolean>> = mapOf(
        "301" to is301Selected,
        "501" to is501Selected
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .width(300.dp)
            .height(120.dp)
            .background(Color.DarkGray)

    ) {
        Column() {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "GAME TYPE",
                fontSize = 16.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {

                CustomButton("301", true, buttonMap)
                Spacer(modifier = Modifier.width(32.dp))
                CustomButton("501", true, buttonMap)
            }
        }
    }
}

@Composable
fun BestOfBox() {
    val isBo1Selected = remember { mutableStateOf(true) }
    val isBo3Selected = remember { mutableStateOf(false) }
    val isBo5Selected = remember { mutableStateOf(false) }
    val buttonMap: Map<String, MutableState<Boolean>> = mapOf(
        "Bo1" to isBo1Selected,
        "Bo3" to isBo3Selected,
        "Bo5" to isBo5Selected
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .width(300.dp)
            .height(120.dp)
            .background(Color.DarkGray)

    ) {
        Column() {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "BEST OF",
                fontSize = 16.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                CustomButton(text = "Bo1", false, buttonMap = buttonMap)
                Spacer(modifier = Modifier.width(18.dp))
                CustomButton(text = "Bo3", false, buttonMap = buttonMap)
                Spacer(modifier = Modifier.width(18.dp))
                CustomButton(text = "Bo5", false, buttonMap = buttonMap)
            }
        }
    }
}

@Composable
fun ChoosePlayersBox() {
    val isPlayerOneSelected = remember { mutableStateOf(true) }
    val isPlayerTwoSelected = remember { mutableStateOf(false) }
    val buttonMap: Map<String, MutableState<Boolean>> = mapOf(
        "1 Player" to isPlayerOneSelected,
        "2 Players" to isPlayerTwoSelected
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .width(300.dp)
            .height(120.dp)
            .background(Color.DarkGray)

    ) {
        Column() {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "PLAYERS",
                fontSize = 16.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                CustomButton(text = "1 Player", true, buttonMap = buttonMap)
                Spacer(modifier = Modifier.width(32.dp)) // Add spacing here
                CustomButton(text = "2 Players", true, buttonMap = buttonMap)
            }
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    bigButton: Boolean,
    buttonMap: Map<String, MutableState<Boolean>>
) {
    OutlinedButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .size(
                width = if (bigButton) 115.dp else 80.dp, // Changing based on "bigButton"
                height = 40.dp
            ),
        onClick = {
            buttonMap.forEach { (buttonText, buttonBool) ->
                buttonBool.value = (text == buttonText)
            }

        },
        border = if (buttonMap[text]?.value == true) {
            BorderStroke(2.dp, Color.Yellow)
        } else {
            BorderStroke(0.dp, Color.Transparent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)

    ) {
        Text(text = text, color = Color.White)
    }
}

//@Composable
//@Preview(showBackground = true)
//fun GameTypePreview() {
//    GameTypeBox()
//}