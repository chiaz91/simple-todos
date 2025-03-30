package com.cy.practice.todo.data.repository

import com.cy.practice.todo.data.local.TaskDao
import com.cy.practice.todo.data.mapper.toEntity
import com.cy.practice.todo.data.mapper.toModelList
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultTaskRepository(
    private val taskDao: TaskDao,
) : TaskRepository {
    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override fun observeTaskList(): Flow<List<Task>> {
        return taskDao.observeTaskList().map { it.toModelList() }
    }
}