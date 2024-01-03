package com.example.kastapil.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kastapil.R
import com.example.kastapil.models.GameInfo
import com.example.kastapil.navigation.Screen
import com.example.kastapil.viewModels.CreateGameViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateGameScreen(
    navController: NavController,
    viewModel: CreateGameViewModel
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) { keyboardController?.hide() }

    ) {
        // Go back button "<-"
        IconButton(
            onClick = {
                navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                }
           },
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.go_back_button),
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }

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

        // CREATE GAME BUTTON
        Button(
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .size(width = 210.dp, height = 60.dp)
                .fillMaxWidth(),

            onClick = {
                if (viewModel.validateNames()) {
                    val gameInfo: GameInfo = viewModel.createGame()
                    navController.navigate(route = Screen.inGame.passGameInfo(gameInfo))
                }
                else {
                    // Error handling or smth
                    Log.d("errorM", "THIS IS A VERY IMPORTANT ERROR MESSAGE")
                }

            }) {
            Text(
                text = stringResource(R.string.create_game_button),
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

                CustomButton(
                    text = stringResource(R.string.game_type_301),
                    bigButton = true,
                    isSelected = is301Selected,
                    onSelected = { viewModel.selectGameType(301) }
                )

                Spacer(modifier = Modifier.width(32.dp))

                CustomButton(
                    stringResource(R.string.game_type_501),
                    bigButton = true,
                    isSelected = is501Selected,
                    onSelected = { viewModel.selectGameType(501) }
                )
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
                text = stringResource(R.string.best_of),
                fontSize = 16.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                CustomButton(
                    text = stringResource(R.string.best_of_one),
                    bigButton = false,
                    isSelected = isBo1Selected,
                    onSelected = { viewModel.selectBestOf(5) }
                )

                Spacer(modifier = Modifier.width(18.dp))

                CustomButton(
                    text = stringResource(R.string.best_of_three),
                    bigButton = false,
                    isSelected = isBo3Selected,
                    onSelected = { viewModel.selectBestOf(3) }
                )

                Spacer(modifier = Modifier.width(18.dp))

                CustomButton(
                    text = stringResource(R.string.best_of_five),
                    bigButton = false,
                    isSelected = isBo5Selected,
                    onSelected = { viewModel.selectBestOf(1) }
                )
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

                CustomButton(
                    text = stringResource(R.string.one_player),
                    bigButton = true,
                    isSelected = isPlayerOneSelected,
                    onSelected = { viewModel.selectPlayers(1) }
                    )

                Spacer(modifier = Modifier.width(32.dp))

                CustomButton(
                    text = stringResource(R.string.two_players),
                    bigButton = true,
                    isSelected = isPlayerTwoSelected,
                    onSelected = { viewModel.selectPlayers(2) }
                )
            }

            CustomTextField(stringResource(R.string.one_player), viewModel)
            if (isPlayerTwoSelected) {
                CustomTextField(stringResource(R.string.two_players), viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(player: String, viewModel: CreateGameViewModel) {
    val playerOneStringResource = stringResource(R.string.one_player)
    val placeHolder: String
    val textState: MutableState<String>
    if (player == playerOneStringResource) {
        textState = viewModel.playerOneName
        placeHolder = stringResource(R.string.player_one_placeholder)
    } else {
        textState = viewModel.playerTwoName
        placeHolder = stringResource(R.string.player_two_placeholder)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField (
        value = textState.value,
        onValueChange = { value ->
            if (player == playerOneStringResource) {
                viewModel.onNameOneChanged(value)
            }
            else {
                viewModel.onNameTwoChanged(value)
            }
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
        placeholder = { Text(text = placeHolder)},
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done // Set the IME action to Done or any other appropriate action
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide() // Hide the keyboard
            })
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