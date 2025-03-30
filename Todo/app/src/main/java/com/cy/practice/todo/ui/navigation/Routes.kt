package com.cy.practice.todo.ui.navigation

import kotlinx.serialization.Serializable


sealed interface Routes {
    @Serializable
    data object TaskList : Routes

    @Serializable
    data object AddTask : Routes
}