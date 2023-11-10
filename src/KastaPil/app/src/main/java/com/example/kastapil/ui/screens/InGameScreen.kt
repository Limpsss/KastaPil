package com.example.kastapil.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Type: " + gameInfo["gameType"].toString(),
                fontSize = 24.sp
            )
            Text(
                text = "Best Of: " + gameInfo["bestOf"].toString(),
                fontSize = 24.sp
            )
            Text(
                text = "Player Amount " + gameInfo["playerAmount"].toString(),
                fontSize = 24.sp
            )
            Text(
                text = "Player 1 Name: " + gameInfo["playerOneName"].toString(),
                fontSize = 24.sp
            )
            Text(
                text = "Player 2 Name: " + gameInfo["playerTwoName"].toString(),
                fontSize = 24.sp
            )
        }

    }
}
