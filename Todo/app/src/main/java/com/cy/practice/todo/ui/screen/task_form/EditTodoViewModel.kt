package com.cy.practice.todo.ui.screen.task_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cy.practice.todo.common.DispatcherProvider
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.domain.repository.TaskRepository
import com.cy.practice.todo.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TaskRepository,
    private val dispatcherProvider: DispatcherProvider,
) : TaskFormViewModel() {

    private lateinit var editingTask: Task

    init {
        val route = savedStateHandle.toRoute<Routes.EditTask>()
        loadTaskData(route.taskId)
    }

    private fun loadTaskData(taskId: Long) {
        viewModelScope.launch {
            editingTask = withContext(dispatcherProvider.io) {
                repository.getTaskById(taskId) ?: throw NullPointerException()
            }

            _uiState.update {
                it.copy(
                    name = editingTask.name,
                    note = editingTask.note,
                    isEditMode = true,
                )
            }
        }
    }

    override fun saveTask() {
        viewModelScope.launch {
            val state = _uiState.value
            val task = editingTask.copy(
                name = state.name,
                note = state.note,
            )
            repository.updateTask(task)
            _event.send(TaskFormEvent.TaskSaved)
        }
    }
}