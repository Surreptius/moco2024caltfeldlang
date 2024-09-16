package edu.moco.pomtodoro.logic

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.moco.pomtodoro.data.TimerRepository
import edu.moco.pomtodoro.data.todo.TodoItem
import edu.moco.pomtodoro.data.todo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: TodoRepository,
) : ViewModel() {
    // Timer Stuff
    private val _timerData = MutableStateFlow(TimerRepository)
    val timerData: StateFlow<TimerRepository> = _timerData.asStateFlow()

    private val timerService = TimerService::class.java
    fun startTimer(context: Context) {
        Intent(context, timerService).also {
            it.action = TimerService.Action.START.toString()
            context.startForegroundService(it)
        }
    }

    fun stopTimer(context: Context) {
        Intent(context, timerService).also {
            it.action = TimerService.Action.STOP.toString()
            context.startForegroundService(it)
        }
    }

    fun restartTimer(context: Context) {
        Intent(context, timerService).also {
            it.action = TimerService.Action.RESTART.toString()
            context.startForegroundService(it)
        }
    }

    // TodoList Stuff
    private val ioDispatcher = Dispatchers.IO

    val todos = repository.allTodos

    fun addTodo(title: String) = viewModelScope.launch(ioDispatcher) {
        repository.insert(TodoItem(title = title))
    }

    fun toggleTodo(todoItem: TodoItem) = viewModelScope.launch(ioDispatcher) {
        repository.insert(todoItem.copy(isDone = !todoItem.isDone))
    }

    fun removeTodo(todoItem: TodoItem) = viewModelScope.launch(ioDispatcher) {
        repository.delete(todoItem)
    }

    // Sensor
    private val accelerometerService = AccelerometerService::class.java

    fun toggleConcentration(context: Context, _action: Boolean) {
        val action =
            if (_action) AccelerometerService.Action.STOP else AccelerometerService.Action.START

        Intent(context, accelerometerService).also {
            it.action = action.toString()
            context.startForegroundService(it)
        }
    }


    // Helper
    fun restartButtonVisibilityControl(
        isWorking: Boolean,
        totalWorkingSeconds: Long,
        totalBreakSeconds: Long,
        currentSeconds: Long
    ): Boolean {
        return if (isWorking && currentSeconds < totalWorkingSeconds) true
        else if (!isWorking && currentSeconds < totalBreakSeconds) true
        else false
    }
}