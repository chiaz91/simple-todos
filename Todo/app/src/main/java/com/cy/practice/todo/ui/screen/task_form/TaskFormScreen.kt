package com.cy.practice.todo.ui.screen.task_form

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.ui.theme.TodoTheme
import com.cy.practice.todo.util.ObserveAsEvents


@Composable
fun TaskFormScreen(
    onSaved: (Boolean) -> Unit,
    vm: TaskFormViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(vm.event) { event ->
        when (event) {
            is TaskFormEvent.TaskSaved -> {
                onSaved(true)
            }

            is TaskFormEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    TaskFormScreen(uiState, vm::onAction, onSaved)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    uiState: TaskFormState,
    onAction: (TaskFormAction) -> Unit,
    onSaved: (Boolean) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Task") },

                navigationIcon = {
                    IconButton(onClick = { onSaved(false) }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .systemBarsPadding()
                .imePadding(),
        ) {
            val titleStyle = LocalTextStyle.current.copy(
                fontSize = 30.sp,
                lineHeight = 30.sp
            )
            TextField(
                value = uiState.name,
                onValueChange = { onAction(TaskFormAction.UpdateName(it)) },
                placeholder = {
                    Text(
                        "What do you need to do?",
                        style = titleStyle
                    )
                },
                textStyle = titleStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
            TextField(
                value = uiState.note,
                onValueChange = { onAction(TaskFormAction.UpdateNote(it)) },
                label = { Text("Note") },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
            TextButton(
                onClick = { onAction(TaskFormAction.Save) },
                enabled = uiState.name.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TaskScreenPreview(modifier: Modifier = Modifier) {
    val state = TaskFormState()
    TodoTheme {
        TaskFormScreen(uiState = state, onAction = {}, onSaved = {})
    }
}

