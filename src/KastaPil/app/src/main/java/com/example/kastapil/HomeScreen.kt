package com.example.kastapil

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(
    navController: NavController
) {
    val animatedBlur = animateDpAsState(targetValue = 30.dp, label = "")
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        val painter1 = painterResource(id = R.drawable.darkgray2)
        Image(
            painter = painter1,
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = animatedBlur.value),
                contentScale = ContentScale.Crop
        )

        val painter2 = painterResource(id = R.drawable.dart2)
        Image(
            painter = painter2,
            contentDescription = "dartboard",
            modifier = Modifier
                .fillMaxWidth(),
        )

        Button(
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .size(width = 210.dp, height = 60.dp)
                .fillMaxWidth(),

            onClick = {
                navController.navigate(route = Screen.CreateGame.route)
            }) {
            Text(
                text = "Create Game",
                fontSize = 24.sp
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun HomeScreenPreview() {
//    HomeScreen(
//        navController = rememberNavController()
//    )
//}