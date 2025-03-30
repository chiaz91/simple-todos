package com.cy.practice.todo.ui.screen.task_list

import com.cy.practice.todo.domain.model.Task


sealed class TaskListAction {
    data object AddTask : TaskListAction()
    data class EditTask(val task: Task) : TaskListAction()
    data class DeleteTask(val task: Task) : TaskListAction()
}