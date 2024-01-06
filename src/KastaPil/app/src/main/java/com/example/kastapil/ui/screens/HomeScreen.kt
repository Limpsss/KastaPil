package com.example.kastapil.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.kastapil.ui.components.AppBar
import com.example.kastapil.ui.components.CustomDialog
import com.example.kastapil.viewModels.HomeViewModel
import kotlinx.coroutines.launch

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    var openStatisticsDialog by remember { mutableStateOf(false) }
    val userData = viewModel.userData
    val context = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val items = listOf(
        MenuItem(
            id = "home",
            title = stringResource(id = R.string.home),
            contentDescription = stringResource(id = R.string.home_description),
            icon = Icons.Default.Home,
        ),
        MenuItem(
            id = "statistics",
            title = stringResource(id = R.string.statistics),
            contentDescription = stringResource(id = R.string.statistics_description),
            icon = Icons.Default.Person,
        ),
        MenuItem(
            id = "settings",
            title = stringResource(id = R.string.settings),
            contentDescription = stringResource(id = R.string.settings_description),
            icon = Icons.Default.Settings,
        ),
        MenuItem(
            id = "about",
            title = stringResource(id = R.string.about),
            contentDescription = stringResource(id = R.string.about_description),
            icon = Icons.Default.Info,
        )
    )

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                if(userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .size(110.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if(userData?.username != null) {
                    Text(
                        text = userData.username,
                        modifier = Modifier.align(CenterHorizontally),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            when (item.id) {
                                "home" -> navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) {
                                        inclusive = true
                                    }
                                }
                            }
                            when (item.id) {
                                "statistics" -> {
                                    if (viewModel.isSignedIn) {
                                        navController.navigate(Screen.Statistics.route) {
                                            popUpTo(Screen.Statistics.route) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        openStatisticsDialog = true
                                    }
                                }
                            }
                            when (item.id) {
                                "settings" -> navController.navigate(Screen.Settings.route) {
                                    popUpTo(Screen.Settings.route) {
                                        inclusive = true
                                    }
                                }
                            }
                            // navigate or something
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    title = "DÖMD",
                    icon = Icons.Default.Menu,
                    onMenuIconClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },

            ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {

                    val painter2 = painterResource(id = R.drawable.dart2)
                    Image(
                        painter = painter2,
                        contentDescription = "dartboard",
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }

                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                        .size(width = 210.dp, height = 60.dp)
                        .fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                    onClick = {
                        navController.navigate(route = Screen.CreateGame.route)
                    }) {
                    Text(
                        text = stringResource(R.string.create_game_button),
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if (viewModel.isSignedIn) {
            BackHandler(enabled = true) {
                openAlertDialog = true
            }
        }
        // If back press and if user is signed in and open a dialog
        if (openAlertDialog) {
            val text = stringResource(R.string.signed_out)
            CustomDialog(
                title = stringResource(R.string.sign_out_dialog_title),
                text = stringResource(R.string.sign_out_dialog_description),
                confirmText = stringResource(R.string.sign_out_dialog_confirm),
                dismissText = stringResource(R.string.sign_out_dialog_dismiss),
                onDismissRequest = {
                    openAlertDialog = false
                },
                onConfirmation = {
                    openAlertDialog = false
                    viewModel.signOut()
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            )
        }

        if (openStatisticsDialog) {
            CustomDialog(
                title = stringResource(R.string.statistics_dialog_title),
                text = stringResource(R.string.statistics_dialog_description),
                confirmText = stringResource(R.string.statistics_dialog_confirm),
                showDismissButton = false,
                onConfirmation = {
                    openStatisticsDialog = false
                },
                onDismissRequest = {},
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