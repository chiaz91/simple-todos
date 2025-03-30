package com.cy.practice.todo.ui.screen.task_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.R
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.ui.screen.task_list.component.NewTaskBottomSheet
import com.cy.practice.todo.ui.screen.task_list.component.TaskList
import com.cy.practice.todo.ui.theme.TodoTheme
import com.cy.practice.todo.util.ObserveAsEvents
import kotlinx.coroutines.launch


@Composable
fun TaskListScreen(
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier,
    vm: TaskListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(vm.event) { event ->
        when (event) {
            is TaskListEvent.TaskDeleted -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val lastDeleted = event.task
                val result = snackbarHostState.showSnackbar(
                    message = "Task '${lastDeleted.name}' deleted",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    vm.onAction(TaskListAction.AddTask(lastDeleted))
                }
            }

            else -> Unit
        }
    }

    TaskListScreen(uiState, onAddTask, vm::onAction, modifier, snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskListState,
    onAddTask: () -> Unit,
    onAction: (TaskListAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    var showAddTask by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
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
                onClick = onAddTask,
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
            Task(0, "Do homework", "", true),
            Task(1, "Do exercise", "", false)
        )
    )
    TodoTheme {
        TaskListScreen(
            state = state,
            onAddTask = {},
            onAction = {}
        )
    }
}