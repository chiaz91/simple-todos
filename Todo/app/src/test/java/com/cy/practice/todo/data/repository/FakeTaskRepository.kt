package com.cy.practice.todo.data.repository

import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTaskRepository(
    defaultTodos: List<Task> = emptyList(),
) : TaskRepository {
    private val tasksFlow = MutableStateFlow(defaultTodos)

    fun setTask(todos: List<Task>) {
        tasksFlow.value = todos
    }

    override suspend fun addTask(task: Task) {
        tasksFlow.value += task
    }

    override suspend fun updateTask(task: Task) {
        tasksFlow.value = tasksFlow.value.map {
            if (it.id == task.id) task else it
        }
    }

    override suspend fun deleteTask(task: Task) {
        tasksFlow.value = tasksFlow.value.filter { it.id != task.id }
    }

    override suspend fun getTaskById(taskId: Long): Task? {
        return tasksFlow.value.find { it.id == taskId }
    }

    override fun observeTaskList(): Flow<List<Task>> = tasksFlow
}