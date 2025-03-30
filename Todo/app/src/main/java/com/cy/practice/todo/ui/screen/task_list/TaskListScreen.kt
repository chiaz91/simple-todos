package com.cy.practice.todo.ui.screen.task_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.domain.model.Task


@Composable
fun TaskListScreen(modifier: Modifier = Modifier, vm: TaskListViewModel = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    TaskListScreen(uiState, vm::onAction, modifier)
}

@Composable
fun TaskListScreen(
    state: TaskListState,
    onAction: (TaskListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.imePadding()) {
        TaskList(
            state.tasks,
            { task, isChecked ->
                onAction(TaskListAction.EditTask(task.copy(isDone = isChecked)))
            },
            { task ->
                onAction(TaskListAction.DeleteTask(task))
            },
            modifier = Modifier.weight(1f)
        )
        Row {
            TextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Enter new task") },
                textStyle = LocalTextStyle.current.copy(),
                trailingIcon = {
                    IconButton(
                        enabled = name.isNotBlank(),
                        onClick = {
                            val newTask = Task(name = name)
                            onAction(TaskListAction.AddTask(newTask))
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Task",
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
private fun TaskList(
    tasks: List<Task>,
    onCheckChanged: (Task, Boolean) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = "No task found")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(tasks, key = { it.id }) {
                TaskItem(it, onCheckChanged, onDelete, modifier = Modifier.animateItem())
                HorizontalDivider()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskItem(
    task: Task,
    onCheckChanged: (Task, Boolean) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = if (!task.isDone) {
        LocalTextStyle.current
    } else {
        LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
    }
    ListItem(
        leadingContent = {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { check ->
                    onCheckChanged(task, check)
                },
            )
        },
        headlineContent = {
            Text(
                text = task.name, style = textStyle, maxLines = 2, overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
            .combinedClickable(
                onClick = { onCheckChanged(task, !task.isDone) },
                onLongClick = { onDelete(task) },
            )
    )
}