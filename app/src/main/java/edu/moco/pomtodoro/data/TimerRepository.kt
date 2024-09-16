package edu.moco.pomtodoro.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class TimerData(
    var totalWorkingSeconds: Long = 20,
    var totalBreakSeconds: Long = 10,

    var currentSeconds: Long = totalWorkingSeconds,

    var isRunning: Boolean = false,
    var isWorking: Boolean = true,
    var isConcentrating: Boolean = false
)

object TimerRepository {
    private var _timerData = MutableStateFlow(TimerData())
    val timerData: StateFlow<TimerData> = _timerData

    fun toggleTimer() = _timerData.update { it.copy(isRunning = !it.isRunning) }
    fun reduceTime() = _timerData.update { it.copy(currentSeconds = it.currentSeconds - 1) }
    fun restart() = _timerData.update {
        it.copy(
            currentSeconds = if (it.isWorking) it.totalWorkingSeconds else it.totalBreakSeconds,
        )
    }

    fun switchMode() {
        _timerData.update {
            it.copy(
                isWorking = !it.isWorking,
                currentSeconds = if (it.isWorking) it.totalBreakSeconds else it.totalWorkingSeconds
            )
        }
    }

    fun switchConcentration() {
        _timerData.update {
            it.copy(
                isConcentrating = !it.isConcentrating,
            )
        }
    }
}