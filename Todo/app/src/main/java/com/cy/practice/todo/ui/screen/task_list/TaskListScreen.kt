package com.cy.practice.todo.ui.screen.task_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.domain.model.Task


@Composable
fun TaskListScreen(modifier: Modifier = Modifier, vm: TaskListViewModel = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    TaskListScreen(uiState, modifier)
}

@Composable
fun TaskListScreen(
    state: TaskListState,
    modifier: Modifier = Modifier
) {

    TaskList(state.tasks, { _, _ -> }, modifier = modifier)
}


@Composable
private fun TaskList(
    tasks: List<Task>,
    onCheckChanged: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = "No task found")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(tasks, key = { it.id }) {
                TaskItem(it, onCheckChanged, modifier = Modifier.animateItem())
                HorizontalDivider()
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
    )
}