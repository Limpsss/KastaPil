package com.example.kastapil.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kastapil.R
import com.example.kastapil.navigation.Screen
import com.example.kastapil.viewModels.SettingsViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        // Go back button "<-"
//        IconButton(
//            onClick = {
//                navController.navigate(Screen.Home.route) {
//                    popUpTo(Screen.Home.route) {
//                        inclusive = true
//                    }
//                }
//            },
//            modifier = Modifier
//                .align(Alignment.TopStart)
//        ) {
//            Icon(
//                imageVector = Icons.Rounded.ArrowBack,
//                contentDescription = stringResource(R.string.go_back_button),
//                modifier = Modifier.size(32.dp),
//                tint = Color.White
//            )
//        }
//
//        Text(
//            text = stringResource(R.string.settings),
//            modifier = Modifier
//                .align(Alignment.TopCenter),
//            color = Color.White,
//            fontSize = 28.sp
//        )
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(R.string.settings))
                    },
                    navigationIcon = {
                        IconButton (
                            onClick = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.go_back_button),
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .width(screenWidth * 0.9f)
                        .height(170.dp)
                        .background(Color.White)
                ) {
                    Column (modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.appearance),
                            modifier = Modifier.padding(5.dp),
                            color = Color.Gray,
                            fontSize = 18.sp
                        )

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                               text = stringResource(R.string.theme),
                               modifier = Modifier
                                   .padding(start = 5.dp)
                                   .weight(1f),
                               color = Color.Black,
                               fontSize = 18.sp
                            )

                            val themes = arrayOf("Auto", "DömD Light", "DömD Dark")
                            var selectedTheme by remember { mutableStateOf(themes.first()) }
                            CustomDropdownMenu(
                                modifier = Modifier.weight(1.2f),
                                items = themes,
                                selectedItem = selectedTheme,
                                onItemSelected = { theme -> selectedTheme = theme }
                            )
                        }

                       Row (
                           modifier = Modifier.fillMaxWidth(),
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Text(
                               text = stringResource(R.string.language),
                               modifier = Modifier
                                   .padding(start = 5.dp)
                                   .weight(1f),
                               color = Color.Black,
                               fontSize = 18.sp
                           )

                           val languages = arrayOf(stringResource(R.string.english), stringResource(R.string.swedish))
                           var selectedLanguage by remember { mutableStateOf(languages.first()) }
                           CustomDropdownMenu(
                               modifier = Modifier.weight(1.2f),
                               items = languages,
                               selectedItem = selectedLanguage,
                               onItemSelected = { lang -> selectedLanguage = lang }
                           )
                       }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .width(screenWidth * 0.9f)
                        .height(100.dp)
                        .background(Color.White)
                ) {
                    Column (modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.username),
                            modifier = Modifier.padding(5.dp),
                            color = Color.Gray,
                            fontSize = 18.sp
                        )

                        TextField(
                            value = viewModel.username.value,
                            onValueChange = { value -> viewModel.onUsernameChange(value) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 18.sp
                            ),
                            placeholder = { Text(text = stringResource(R.string.placeholder_username)) },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                containerColor = Color.White,
                                focusedTrailingIconColor = Color.Black,
                                unfocusedTrailingIconColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                                focusedIndicatorColor = Color.Black
                            ),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    modifier: Modifier,
    items: Array<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Color.White,
                focusedTrailingIconColor = Color.Black,
                unfocusedTrailingIconColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item, color = Color.Black) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


