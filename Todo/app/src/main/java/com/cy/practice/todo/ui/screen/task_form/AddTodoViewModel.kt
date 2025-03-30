package com.cy.practice.todo.ui.screen.task_form

import androidx.lifecycle.viewModelScope
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
) : TaskFormViewModel() {

    override fun saveTask() {
        viewModelScope.launch {
            val state = _uiState.value
            val task = Task(
                name = state.name,
                note = state.note,
            )
            repository.addTask(task)
            _event.send(TaskFormEvent.TaskSaved)
        }
    }
}