package com.cy.practice.todo.ui.screen.task_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class TaskFormViewModel : ViewModel() {
    protected val _uiState = MutableStateFlow(TaskFormState())
    val uiState = _uiState.asStateFlow()

    protected val _event = Channel<TaskFormEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: TaskFormAction) {
        when (action) {
            is TaskFormAction.UpdateName -> _uiState.update { it.copy(name = action.name) }
            is TaskFormAction.UpdateNote -> _uiState.update { it.copy(note = action.note) }
            TaskFormAction.Save -> attemptSaving()
        }
    }

    private fun attemptSaving() {
        val error = validateForm()
        if (error != null) {
            viewModelScope.launch {
                _event.send(TaskFormEvent.Error(error))
            }
            return
        }
        saveTask()
    }

    private fun validateForm(): String? {
        val state = _uiState.value
        return when {
            state.name.isBlank() -> "Name cannot be empty"
            else -> null
        }
    }

    protected abstract fun saveTask()
}