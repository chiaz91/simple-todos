package com.cy.practice.todo.domain.model

data class Task(
    val id: Int = 0,
    val name: String = "",
    val note: String = "",
    val isDone: Boolean = false,
)