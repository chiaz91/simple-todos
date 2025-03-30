package com.cy.practice.todo.ui.screen.task_list

import androidx.lifecycle.ViewModel
import com.cy.practice.todo.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListState())
    val uiState = _uiState.asStateFlow()


    init {
        // create dummy data
        val data = mutableListOf<Task>()
        repeat(10) {
            data.add(Task(id = it, name = "Task ${it + 1}"))
        }
        _uiState.update {
            it.copy(tasks = data)
        }
    }


}