package edu.moco.pomtodoro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import edu.moco.pomtodoro.logic.MainViewModel
import edu.moco.pomtodoro.ui.components.TodoContainer
import edu.moco.pomtodoro.ui.components.TodoInput

@Composable
fun TaskScreen(viewModel: MainViewModel) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TodoContainer(
            todoItems = viewModel.todos,
            onItemClick = viewModel::toggleTodo,
            onItemDelete = viewModel::removeTodo
        )
        TodoInput(
            onAdd = viewModel::addTodo
        )
    }
}

