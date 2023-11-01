package com.example.kastapil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kastapil.navigation.Screen

@Composable
fun SignInScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextFieldView(false)

            TextFieldView(true)

            Button(
                onClick = {
                    navController.navigate(
                        route = Screen.Home.route
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
fun TextFieldView(isPassword: Boolean) {
    val textState = remember { mutableStateOf("") }
    val placeholder = if (isPassword) "Password" else "Username"
    val label = if(isPassword) "Password" else "Username"

    OutlinedTextField (
        value = textState.value,
        onValueChange = {
            textState.value = it
        },
        modifier = Modifier.padding(5.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            unfocusedIndicatorColor = Color.Black,
            focusedIndicatorColor = Color.Blue,

        ),
        shape = RoundedCornerShape(percent = 20),
        placeholder = { Text(text = placeholder)},
        label = {Text(text = label)},
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

//@Composable
//@Preview(showBackground = true)
//fun SignInScreenPreview() {
//    SignInScreen(navController = rememberNavController())
//}