package edu.moco.pomtodoro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import edu.moco.pomtodoro.data.todo.TodoItem
import edu.moco.pomtodoro.ui.theme.dp_4
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun TodoContainer(
    modifier: Modifier = Modifier,
    todoItems: Flow<List<TodoItem>> = flowOf(listOf()),
    onItemClick: (TodoItem) -> Unit = {},
    onItemDelete: (TodoItem) -> Unit = {},
) {
    val todos = todoItems.collectAsState(initial = listOf()).value

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(dp_4),
        verticalArrangement = Arrangement.spacedBy(dp_4)
    ) {
        items(todos, key = { it.id }) { item ->
            TodoItemUi(
                todoItem = item,
                onItemClick = onItemClick,
                onItemDelete = onItemDelete
            )
        }
    }
}
