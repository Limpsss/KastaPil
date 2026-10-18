package com.example.kastapil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kastapil.R
import com.example.kastapil.models.GameInfo
import com.example.kastapil.navigation.Screen
import com.example.kastapil.ui.components.AppBar
import com.example.kastapil.viewModels.InGameViewModel
import com.example.kastapil.viewModels.InGameViewModelFactory
import com.example.kastapil.viewModels.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    navController: NavController
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(R.string.statistics),
                icon = Icons.Default.ArrowBack,
                onMenuIconClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        // Main column
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            // General stats box
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp, 0.dp, 8.dp, 0.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column (
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Title of box
                    Text(
                        text = "General statistics",
                        modifier = Modifier.padding(bottom = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp
                    )

                    StatRow(
                        text = "Darts thrown",
                        data = "${viewModel.getDartsThrown()}",
                        rowColor = MaterialTheme.colorScheme.primaryContainer
                    )

                    StatRow(
                        text = "Most score hit",
                        data = "${viewModel.getMostHitNumber()?.first} : ${viewModel.getMostHitNumber()?.second}",
                        rowColor = MaterialTheme.colorScheme.secondaryContainer
                    )

                    StatRow(
                        text = "Most double out",
                        data = "${viewModel.getMostDoubleOut()?.first} : ${viewModel.getMostDoubleOut()?.second}",
                        rowColor = MaterialTheme.colorScheme.primaryContainer
                    )

                    StatRow(
                        text = "Most single out",
                        data = "${viewModel.getMostSingleOut()?.first} : ${viewModel.getMostSingleOut()?.second}",
                        rowColor = MaterialTheme.colorScheme.secondaryContainer
                    )

//                    for (i in 1..10) {
//                        StatRow(
//                            text = "Darts thrown",
//                            data = "69",
//                            rowColor = if ( i % 2 == 0 )
//                                MaterialTheme.colorScheme.secondaryContainer else
//                                    MaterialTheme.colorScheme.primaryContainer
//                        )
//                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Average stats box
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp, 0.dp, 8.dp, 0.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Title of box
                    Text(
                        text = "Other statistics",
                        modifier = Modifier.padding(bottom = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp
                    )

                    for (i in 1..8) {
                        StatRow(
                            text = "Test",
                            data = "420",
                            rowColor = if ( i % 2 == 0 )
                                MaterialTheme.colorScheme.secondaryContainer else
                                    MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StatRow(
    text: String,
    data: String,
    rowColor: Color
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = rowColor)
            .padding(4.dp)
    ) {

        Text(
            text = text,
            color = Color.Gray,
            fontSize = 18.sp,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = data,
            color = Color(0xFF00a000),
            fontSize = 18.sp,
        )
    }
}

@Preview
@Composable
fun StatisticsScreenPreview() {
    val mockNavController = rememberNavController()
    val mockViewModel: StatisticsViewModel = viewModel()
    StatisticsScreen(viewModel = mockViewModel, navController = mockNavController)
}