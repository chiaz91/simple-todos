package com.cy.practice.todo.ui.screen.task_list

import com.cy.practice.todo.domain.model.Task


sealed class TaskListEvent {
    data object TaskSaved : TaskListEvent()
    data class TaskDeleted(val task: Task) : TaskListEvent()
}