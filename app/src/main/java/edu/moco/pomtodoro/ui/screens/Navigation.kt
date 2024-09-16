package edu.moco.pomtodoro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.moco.pomtodoro.R
import edu.moco.pomtodoro.logic.MainViewModel
import edu.moco.pomtodoro.ui.theme.OnSecondaryContainer
import edu.moco.pomtodoro.ui.theme.SecondaryContainer

@Composable
fun Navigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomAppBar(navController = navController, viewModel = viewModel) },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController,
            startDestination = "Timer"
        ) {
            composable("Timer") {
                TimerScreen(viewModel = viewModel)
            }
            composable("Task") {
                TaskScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BottomAppBar(navController: NavController, viewModel: MainViewModel) {
    val timeData by viewModel.timerData.collectAsStateWithLifecycle()
    val time by timeData.timerData.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavButton(
                    iconR = R.drawable.hourglass,
                    descR = R.string.nav_timer,
                    currentRoute == "Timer"
                ) {
                    navController.navigate("Timer") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                NavButton(
                    iconR = R.drawable.task_list,
                    descR = R.string.nav_todo,
                    currentRoute == "Task"
                ) {
                    navController.navigate("Task") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (time.isRunning)
                        viewModel.stopTimer(context)
                    else
                        viewModel.startTimer(context)
                }
            ) {
                Icon(
                    painter = painterResource(
                        id =
                        if (time.isRunning)
                            R.drawable.control_pause
                        else
                            R.drawable.control_start
                    ),
                    contentDescription =
                    if (time.isRunning)
                        "Stop Timer"
                    else
                        "Start Timer"
                )
            }
        }
    )
}

@Composable
fun NavButton(iconR: Int, descR: Int, isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) SecondaryContainer else Color.Transparent
        )
    ) {
        Icon(
            painter = painterResource(id = iconR),
            contentDescription = stringResource(id = descR),
            tint = OnSecondaryContainer
        )
    }
}