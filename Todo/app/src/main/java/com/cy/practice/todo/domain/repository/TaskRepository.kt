package com.cy.practice.todo.domain.repository

import com.cy.practice.todo.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun getTaskById(taskId: Long): Task
    fun observeTaskList(): Flow<List<Task>>
}