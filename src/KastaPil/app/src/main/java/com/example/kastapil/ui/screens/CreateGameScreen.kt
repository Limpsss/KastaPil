package com.example.kastapil.ui.screens

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kastapil.navigation.Screen
import com.example.kastapil.viewModels.CreateGameViewModel

@Composable
fun CreateGameScreen(navController: NavController, viewModel: CreateGameViewModel) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)

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
            GameTypeBox(viewModel)
            Spacer(modifier = Modifier.height(30.dp))
            BestOfBox(viewModel)
            Spacer(modifier = Modifier.height(30.dp))
            ChoosePlayersBox(viewModel)
        }

        Button(
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .size(width = 210.dp, height = 60.dp)
                .fillMaxWidth(),

            onClick = {
                val gameInfo = viewModel.createGame()
                navController.navigate(route = Screen.inGame.passGameInfo(gameInfo))

            }) {
            Text(
                text = "Create Game",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun GameTypeBox(viewModel: CreateGameViewModel) {
    // Using the states from the CreateGameViewModel
    val is301Selected = viewModel.is301Selected.value
    val is501Selected = viewModel.is501Selected.value

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

                CustomButton("301", true, is301Selected) { viewModel.selectGameType("301") }
                Spacer(modifier = Modifier.width(32.dp))
                CustomButton("501", true, is501Selected) { viewModel.selectGameType("501") }
            }
        }
    }
}

@Composable
fun BestOfBox(viewModel: CreateGameViewModel) {
    val isBo1Selected = viewModel.isBo1Selected.value
    val isBo3Selected = viewModel.isBo3Selected.value
    val isBo5Selected = viewModel.isBo5Selected.value

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
                CustomButton(text = "Bo1", false, isBo1Selected) { viewModel.selectBestOf("Bo1") }
                Spacer(modifier = Modifier.width(18.dp))
                CustomButton(text = "Bo3", false, isBo3Selected) { viewModel.selectBestOf("Bo3") }
                Spacer(modifier = Modifier.width(18.dp))
                CustomButton(text = "Bo5", false, isBo5Selected) { viewModel.selectBestOf("Bo5") }
            }
        }
    }
}

@Composable
fun ChoosePlayersBox(viewModel: CreateGameViewModel) {
    val isPlayerOneSelected = viewModel.isPlayerOneSelected.value
    val isPlayerTwoSelected = viewModel.isPlayerTwoSelected.value

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .width(300.dp)
            .fillMaxHeight()
            .background(Color.DarkGray)

    ) {
        Column {
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
                CustomButton(text = "1 Player", true, isPlayerOneSelected) { viewModel.selectPlayers("1 Player") }
                Spacer(modifier = Modifier.width(32.dp))
                CustomButton(text = "2 Players", true, isPlayerTwoSelected) { viewModel.selectPlayers("2 Players") }
            }
            CustomTextField("Player 1")
            if (isPlayerTwoSelected) {
                CustomTextField("Player 2")
            }
        }
    }
}

// Pretty much the same as in SignInScreen, could use the same composable?
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(player: String) {
    val textState = remember { mutableStateOf("") }

    OutlinedTextField (
        value = textState.value,
        onValueChange = {
            textState.value = it
        },
        modifier = Modifier
            .padding(5.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            unfocusedIndicatorColor = Color.Black,
            focusedIndicatorColor = Color.Yellow,
            containerColor = Color.Black

            ),
        shape = RoundedCornerShape(percent = 60),
        placeholder = { Text(text = "$player name...")}
    )
}

@Composable
fun CustomButton(
    text: String,
    bigButton: Boolean,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .size(
                width = if (bigButton) 115.dp else 80.dp, // Changing based on "bigButton"
                height = 40.dp
            ),
        onClick = onSelected,
        border = if (isSelected) {
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