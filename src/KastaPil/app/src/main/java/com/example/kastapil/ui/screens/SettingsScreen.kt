package com.example.kastapil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.kastapil.viewModels.InGameViewModel
import com.example.kastapil.viewModels.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Text(text = "HEJSAN",
            color = Color.White)
    }
}