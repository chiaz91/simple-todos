package com.cy.practice.todo.ui.screen.task_list

import com.cy.practice.todo.domain.model.Task


data class TaskListState(
    val tasks: List<Task> = emptyList(),
)