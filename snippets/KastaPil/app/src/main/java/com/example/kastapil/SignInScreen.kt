package com.example.kastapil

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.format.TextStyle

@Composable
fun SignInScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val textFieldValue1 = remember { mutableStateOf("") }
        val textFieldValue2 = remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextFieldView(textFieldValue1)
            TextFieldView(textFieldValue2)

            Button(
                onClick = {
                    var username = textFieldValue1.value
                    var password = textFieldValue2.value
                    if (username == "") {
                        username = "empty"
                    }
                    if (password == "") {
                        password = "empty"
                    }
                    navController.navigate(
                        route = "home_screen/$username/$password"
                    )
                },
            ) {
                Text(text = "Don't click here")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldView(textState: MutableState<String>) {

    OutlinedTextField (
        value = textState.value,
        onValueChange = {
            textState.value = it
        },
        modifier = Modifier.padding(5.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            unfocusedIndicatorColor = Color.Blue,
            focusedIndicatorColor = Color.Red,

        )
    )
    Log.d("tag69", textState.value)
}

@Composable
@Preview(showBackground = true)
fun SignInScreenPreview() {
    SignInScreen(navController = rememberNavController())
}