package com.example.kastapil.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kastapil.R
import com.example.kastapil.navigation.Screen
import com.example.kastapil.viewModels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    val animatedBlur = animateDpAsState(targetValue = 30.dp, label = "")
    val userData = viewModel.userData
    val context = LocalContext.current

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
        // Settings button top right
        IconButton(
            onClick = {
                navController.navigate(Screen.Settings.route)
           },
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = stringResource(R.string.settings),
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }

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
                onClick = {
                    viewModel.signOut()
                    Toast.makeText(context, "Signed out", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            ){
                Text(text = stringResource(R.string.sign_out_button))
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
                text = stringResource(R.string.create_game_button),
                fontSize = 24.sp
            )
        }
    }

    BackHandler(enabled = true) {
        openAlertDialog = true
    }

    // If back press check if user is signed in and open a dialog
    // If user is a guest just go back to sign in screen
    if (openAlertDialog) {
        if (viewModel.isSignedIn) {
            HomeScreenDialog(
                onDismissRequest = {
                    openAlertDialog = false
                },
                onConfirmation = {
                    openAlertDialog = false
                    viewModel.signOut()
                    handleBackPressHomeScreen(navController)
                }
            )
        }
        else {
            handleBackPressHomeScreen(navController)
        }
    }
}

@Composable
fun HomeScreenDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Icon")
        },
        title = {
            Text(text = stringResource(R.string.alert_dialog_title))
        },
        text = {
            Text(text = stringResource(R.string.alert_dialog_description))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = stringResource(R.string.alert_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.alert_dialog_dismiss))
            }
        }
    )
}

fun handleBackPressHomeScreen(navController: NavController) {
    navController.navigate(Screen.SignIn.route) {
        popUpTo(Screen.SignIn.route) {
            inclusive = true
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