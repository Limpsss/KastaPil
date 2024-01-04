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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.kastapil.ui.components.CustomDropdownMenu
import com.example.kastapil.viewModels.SettingsViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            // Appearance box
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp, 8.dp, 8.dp, 0.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column (
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Title of box
                    Text(
                        text = stringResource(R.string.appearance),
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                           text = stringResource(R.string.theme),
                           modifier = Modifier
                               .weight(1f),
                           color = MaterialTheme.colorScheme.onPrimaryContainer,
                           fontSize = 18.sp
                        )

                        val themes = arrayOf("Auto", "DömD Light", "DömD Dark")
                        val selectedTheme by remember { mutableStateOf(themes.first()) }
                        CustomDropdownMenu(
                            modifier = Modifier.weight(1.2f),
                            items = themes,
                            selectedItem = selectedTheme,
                            onItemSelected = { theme ->
                                if (themes.indexOf(theme) == 0) {
                                    viewModel.changeTheme(null)
                                } else if (themes.indexOf(theme) == 1) {
                                    viewModel.changeTheme(false)
                                } else if (themes.indexOf(theme) == 2) {
                                    viewModel.changeTheme(true)
                                }
                            }
                        )
                    }

                   Row (
                       modifier = Modifier
                           .fillMaxWidth(),
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Text(
                           text = stringResource(R.string.language),
                           modifier = Modifier
                               .weight(1f),
                           color = MaterialTheme.colorScheme.onPrimaryContainer,
                           fontSize = 18.sp
                       )

                       val currLanguage = getCurrUserLanguage(viewModel.currLangCode)
                       val languages = arrayOf(stringResource(R.string.english), stringResource(R.string.swedish))
                       val selectedLanguage by remember { mutableStateOf(currLanguage) }
                       CustomDropdownMenu(
                           modifier = Modifier.weight(1.2f),
                           items = languages,
                           selectedItem = selectedLanguage,
                           onItemSelected = { lang ->
                               if (languages.indexOf(lang) == 0) {
                                   viewModel.changeLanguage("en")
                               } else if (languages.indexOf(lang) == 1) {
                                   viewModel.changeLanguage("sv")
                               }
                           }
                       )
                   }
                }
            }

            // Username box
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp, 8.dp, 8.dp, 0.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column (modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = stringResource(R.string.username),
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
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun getCurrUserLanguage(langCode: String): String {
    var res = ""
    if (langCode == "sv") {
        res = stringResource(R.string.swedish)
    }
    else if (langCode == "en") {
        res = stringResource(R.string.english)
    }
    return res
}


