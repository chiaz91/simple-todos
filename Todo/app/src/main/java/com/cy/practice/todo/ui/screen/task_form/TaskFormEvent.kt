package com.cy.practice.todo.ui.screen.task_form


sealed class TaskFormEvent {
    data object TaskSaved : TaskFormEvent()
    data class Error(val message: String) : TaskFormEvent()
}