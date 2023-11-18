package com.example.kastapil.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kastapil.R
import com.example.kastapil.models.UserData
import com.example.kastapil.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    userData: UserData?,
    onSignOut: () -> Unit
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if(userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if(userData?.username != null) {
                Text(
                    text = userData.username,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                modifier = Modifier
                    .size(width = 110.dp, height = 40.dp),
                onClick = onSignOut
            ){
                Text(text = "Sign out")
            }
        }

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