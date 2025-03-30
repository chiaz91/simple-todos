package com.cy.practice.todo.ui.screen.task_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskFormState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<TaskFormEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: TaskFormAction) {
        when (action) {
            is TaskFormAction.UpdateName -> _uiState.update { it.copy(name = action.name) }
            is TaskFormAction.UpdateNote -> _uiState.update { it.copy(note = action.note) }
            is TaskFormAction.Save -> saveTask()
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state = _uiState.value
            val Task = Task(name = state.name, note = state.note)
            repository.addTask(Task)
            _event.send(TaskFormEvent.TaskSaved)
        }
    }

}