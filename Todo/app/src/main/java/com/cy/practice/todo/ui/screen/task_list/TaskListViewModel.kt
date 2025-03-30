package com.cy.practice.todo.ui.screen.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<TaskListEvent>()
    val event = _event.receiveAsFlow()

    init {
        taskRepository.observeTaskList()
            .onEach { tasks ->
                //tasks.forEach{ Timber.d(it.toString())}

                _uiState.update { state ->
                    state.copy(tasks = tasks.sortedBy { it.isDone })
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: TaskListAction) {
        when (action) {
            is TaskListAction.AddTask -> addTask(action.task)
            is TaskListAction.EditTask -> editTask(action.task)
            is TaskListAction.DeleteTask -> deleteTask(action.task)
        }
    }

    private fun addTask(newTask: Task) {
        viewModelScope.launch {
            taskRepository.addTask(newTask)
            _event.send(TaskListEvent.TaskSaved)
        }
    }

    private fun editTask(updatingTask: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(updatingTask)
            _event.send(TaskListEvent.TaskSaved)
        }
    }

    private fun deleteTask(deletingTask: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(deletingTask)
            _event.send(TaskListEvent.TaskDeleted(deletingTask))
        }
    }


}