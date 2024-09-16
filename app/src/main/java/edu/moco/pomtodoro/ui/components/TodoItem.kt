package edu.moco.pomtodoro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.moco.pomtodoro.R
import edu.moco.pomtodoro.data.todo.TodoItem
import edu.moco.pomtodoro.ui.theme.OnSecondaryContainer
import edu.moco.pomtodoro.ui.theme.SecondaryContainer
import edu.moco.pomtodoro.ui.theme.TextSize
import edu.moco.pomtodoro.ui.theme.dp_16
import edu.moco.pomtodoro.ui.theme.dp_2
import edu.moco.pomtodoro.ui.theme.dp_4

@Composable
fun TodoItemUi(
    todoItem: TodoItem = TodoItem(title = "Todo Item"),
    onItemClick: (TodoItem) -> Unit = {},
    onItemDelete: (TodoItem) -> Unit = {}
) {
    val checkbox =
        if (todoItem.isDone) R.drawable.check_box_selected else R.drawable.check_box_empty

    val lineDecoration = if (todoItem.isDone) TextDecoration.LineThrough else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(dp_16),
        elevation = CardDefaults.cardElevation(defaultElevation = dp_4),
        shape = RoundedCornerShape(dp_2),
        colors = CardDefaults.cardColors(
            containerColor = SecondaryContainer,
            contentColor = OnSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) { onItemClick(todoItem) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = checkbox),
                contentDescription = stringResource(id = R.string.todo_switch_completed),
                modifier = Modifier
                    .padding(dp_2)
            )
            Text(
                text = todoItem.title,
                style = TextSize.base,
                textDecoration = lineDecoration,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { onItemDelete(todoItem) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = stringResource(id = R.string.todo_task_delete)
                )
            }

        }
    }
}

@Preview
@Composable
fun TodoItemUiPreview() {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TodoItemUi(todoItem = TodoItem(title = "Todo Item 1"))
        TodoItemUi(todoItem = TodoItem(title = "Todo Item 2"))
        TodoItemUi(todoItem = TodoItem(title = "Todo Item 3"))
        TodoItemUi(todoItem = TodoItem(title = "Todo Item 4", isDone = true))
    }
}