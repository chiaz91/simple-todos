package com.cy.practice.todo.ui.screen.task_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.ui.component.CustomCheckbox
import com.cy.practice.todo.ui.component.defaultCheckboxColors
import com.cy.practice.todo.ui.theme.TodoTheme


@Composable
fun TaskList(
    tasks: List<Task>,
    onClicked: (Task) -> Unit,
    onCheckChanged: (Task, Boolean) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No task found")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(tasks, key = { it.id }) {
                SwipeableTaskItem(
                    it,
                    onCheckChanged,
                    onDelete,
                    modifier = Modifier
                        .animateItem()
                        .clickable { onClicked(it) }
                )
                // HorizontalDivider()
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onCheckChanged: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        leadingContent = {
            CustomCheckbox(
                task.isDone,
                { onCheckChanged(task, it) },
                colors = defaultCheckboxColors().copy(
                    checkedBackground = Color.Transparent,
                    uncheckedBackground = Color.Transparent,
                    borderColor = MaterialTheme.colorScheme.primary,
                    checkMark = MaterialTheme.colorScheme.primary,
                )
            )
        },
        headlineContent = {
            Text(
                text = task.name,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            if (task.note.isNotEmpty()) {
                Row {
                    Icon(
                        Icons.AutoMirrored.Default.Notes,
                        contentDescription = "note",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = task.note,
                        textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp
                    )
                }
            }
        },
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun SwipeableTaskItem(
    task: Task,
    onCheckChanged: (Task, Boolean) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.3f }
    )

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                onCheckChanged(task, true)
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            SwipeToDismissBoxValue.EndToStart -> {
                onDelete(task)
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            SwipeToDismissBoxValue.Settled -> Unit
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = { TaskItemDismissBackground(dismissState) },
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true
    ) {
        TaskItem(
            task,
            onCheckChanged,
        )
    }
}

@Composable
private fun TaskItemDismissBackground(
    dismissState: SwipeToDismissBoxState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp, 8.dp),
    ) {

        when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Check completed",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            SwipeToDismissBoxValue.EndToStart -> {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            SwipeToDismissBoxValue.Settled -> Unit
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskItemPreview(modifier: Modifier = Modifier) {
    var task by remember {
        mutableStateOf(Task(0, "Task 1", LoremIpsum().toString(), false))
    }

    TodoTheme {
        SwipeableTaskItem(
            task,
            { _, checked -> task = task.copy(isDone = checked) },
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview(modifier: Modifier = Modifier) {
    val taskList = listOf(
        Task(1, "Task 1", "", false),
        Task(2, "Task 2", LoremIpsum().toString(), false),
        Task(3, "Task 3", LoremIpsum().toString(), true),
    )

    TodoTheme {
        TaskList(taskList, {}, { _, _ -> }, {})
    }
}