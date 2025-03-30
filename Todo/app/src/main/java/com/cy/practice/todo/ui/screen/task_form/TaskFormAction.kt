package com.cy.practice.todo.ui.screen.task_form


sealed class TaskFormAction {
    data class UpdateName(val name: String) : TaskFormAction()
    data class UpdateNote(val note: String) : TaskFormAction()
    data object Save : TaskFormAction()
}