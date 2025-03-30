package com.cy.practice.todo.data.mapper

import com.cy.practice.todo.data.local.TaskEntity
import com.cy.practice.todo.domain.model.Task


fun TaskEntity.toModel() = Task(
    id, name, note, isDone
)

fun Task.toEntity() = TaskEntity(
    id, name, note, isDone
)

fun List<TaskEntity>.toModelList(): List<Task> {
    return this.map { it.toModel() }
}