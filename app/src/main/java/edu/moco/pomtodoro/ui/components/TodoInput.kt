package edu.moco.pomtodoro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import edu.moco.pomtodoro.R
import edu.moco.pomtodoro.ui.theme.OnSecondaryColor
import edu.moco.pomtodoro.ui.theme.OnSecondaryContainer
import edu.moco.pomtodoro.ui.theme.SecondaryColor
import edu.moco.pomtodoro.ui.theme.SecondaryContainer
import edu.moco.pomtodoro.ui.theme.dp_2
import edu.moco.pomtodoro.ui.theme.dp_20
import edu.moco.pomtodoro.ui.theme.dp_4
import edu.moco.pomtodoro.ui.theme.dp_8

@Composable
fun TodoInput(
    modifier: Modifier = Modifier,
    onAdd: (String) -> Unit = {}
) {
    var input by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(size = dp_2),
        modifier = modifier
            .fillMaxWidth()
            .height(dp_20)
            .padding(dp_2),
        elevation = CardDefaults.cardElevation(defaultElevation = dp_4)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SecondaryColor)
                .padding(end = dp_4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = input,
                onValueChange = { newInput -> input = newInput },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.todo_input_placeholder),
                        color = OnSecondaryColor.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = OnSecondaryColor,
                    unfocusedTextColor = OnSecondaryColor,
                    cursorColor = OnSecondaryColor,
                )
            )
            FloatingActionButton(
                containerColor = SecondaryContainer,
                onClick = {
                    if (input.isEmpty()) return@FloatingActionButton
                    onAdd(input)
                    input = ""
                },
                modifier = Modifier.size(dp_8),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = null,
                    tint = OnSecondaryContainer
                )
            }
        }
    }
}