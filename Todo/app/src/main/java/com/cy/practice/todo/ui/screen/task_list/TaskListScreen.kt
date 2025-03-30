package com.cy.practice.todo.ui.screen.task_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.ui.screen.task_list.component.TaskList
import com.cy.practice.todo.ui.theme.TodoTheme


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
            { task ->
                onAction(TaskListAction.EditTask(task.copy(isDone = !task.isDone)))
            },
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


@Preview(showBackground = true)
@Composable
fun TaskScreenPreview(modifier: Modifier = Modifier) {
    val state = TaskListState(
        tasks = listOf(
            Task(1, "Task 1", "", false),
            Task(2, "Task 2", "With note", true)
        )
    )
    TodoTheme {
        TaskListScreen(
            state = state,
            onAction = {}
        )
    }
}