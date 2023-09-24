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

                CustomButton("301", is301Selected, is501Selected)
                Spacer(modifier = Modifier.width(32.dp))
                CustomButton("501", is501Selected, is301Selected)
            }
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    thisSelected: MutableState<Boolean>,
    otherSelected: MutableState<Boolean>
) {

    OutlinedButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .size(width = 110.dp, height = 40.dp),
        onClick = {
            thisSelected.value = true
            otherSelected.value = false
        },
        border = if (thisSelected.value) BorderStroke(2.dp, Color.Yellow) else BorderStroke(0.dp, Color.Transparent),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)

    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun BestOfBox() {
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
                Button(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "BO1")
                }

                Spacer(modifier = Modifier.width(24.dp)) // Add spacing here

                Button(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    onClick = { /*TODO*/ }
                ){
                    Text(text = "BO3")
                }

                Spacer(modifier = Modifier.width(24.dp)) // Add spacing here

                Button(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    onClick = { /*TODO*/ }
                ){
                    Text(text = "BO5")
                }
            }
        }
    }
}

@Composable
fun ChoosePlayersBox() {
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
                Button(
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "301")
                }

                Spacer(modifier = Modifier.width(32.dp)) // Add spacing here

                Button(
                    onClick = { /*TODO*/ }
                ){
                    Text(text = "501")
                }
            }
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun GameTypePreview() {
//    GameTypeBox()
//}