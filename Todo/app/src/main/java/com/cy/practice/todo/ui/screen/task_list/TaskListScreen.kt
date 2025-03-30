package com.cy.practice.todo.ui.screen.task_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.ui.screen.task_list.component.NewTaskBottomSheet
import com.cy.practice.todo.ui.screen.task_list.component.TaskList
import com.cy.practice.todo.ui.theme.TodoTheme


@Composable
fun TaskListScreen(modifier: Modifier = Modifier, vm: TaskListViewModel = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    TaskListScreen(uiState, vm::onAction, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskListState,
    onAction: (TaskListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTask by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.imePadding()) {
        TaskList(
            state.tasks,
            { task ->
                onAction(TaskListAction.EditTask(task.copy(isDone = !task.isDone)))
            },
            { task, isChecked ->
                onAction(TaskListAction.EditTask(task.copy(isDone = isChecked)))
            },
            { task -> onAction(TaskListAction.DeleteTask(task)) },
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = { showAddTask = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add new task",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("Add Task")
        }
    }

    if (showAddTask) {
        NewTaskBottomSheet(
            { showAddTask = false },
            { task -> onAction(TaskListAction.AddTask(task)) }
        )
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