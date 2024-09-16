package edu.moco.pomtodoro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.moco.pomtodoro.R
import edu.moco.pomtodoro.logic.MainViewModel
import edu.moco.pomtodoro.ui.theme.OnSecondaryContainer
import edu.moco.pomtodoro.ui.theme.SecondaryContainer
import edu.moco.pomtodoro.ui.theme.TextSize
import edu.moco.pomtodoro.ui.theme.dp_16
import edu.moco.pomtodoro.ui.theme.dp_2
import edu.moco.pomtodoro.ui.theme.dp_4
import edu.moco.pomtodoro.ui.theme.dp_64
import edu.moco.pomtodoro.ui.theme.dp_8
import edu.moco.pomtodoro.utils.formatTimer

@Composable
fun TimerScreen(viewModel: MainViewModel) {
    val timerData by viewModel.timerData.collectAsStateWithLifecycle()
    val time by timerData.timerData.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
    ) {
        CircularDeterminateIndicator(
            totalSeconds =
            if (time.isWorking) time.totalWorkingSeconds
            else time.totalBreakSeconds,
            currentSeconds = time.currentSeconds
        )
        Text(
            text = formatTimer(time.currentSeconds),
            style = TextSize.xxl
        )
        if (viewModel.restartButtonVisibilityControl(
                time.isWorking,
                time.totalWorkingSeconds,
                time.totalBreakSeconds,
                time.currentSeconds
            )
        )
            IconButton(
                onClick = { viewModel.restartTimer(context) },
                modifier = Modifier
                    .offset(y = dp_16)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.restart),
                    contentDescription = stringResource(id = R.string.timer_restart),
                    modifier = Modifier
                        .size(dp_8)
                )
            }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp_4)
                .offset(y = dp_64),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dp_2)
        ) {
            Text(text = "Toggle Concentration Mode")
            Spacer(modifier = Modifier.width(dp_8))
            Switch(
                checked = time.isConcentrating,
                onCheckedChange = {
                    viewModel.toggleConcentration(context, time.isConcentrating)
                },
            )
        }
    }
}


@Composable
fun CircularDeterminateIndicator(totalSeconds: Long, currentSeconds: Long) {
    CircularProgressIndicator(
        progress = { 1f - (currentSeconds.toFloat() / totalSeconds.toFloat()) },
        modifier = Modifier
            .size(dp_64),
        color = OnSecondaryContainer,
        trackColor = SecondaryContainer,
        strokeWidth = dp_2
    )
}