package com.cy.practice.todo.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FakeTaskDao(
    defaultTasks: List<TaskEntity> = emptyList()
) : TaskDao {
    private val tasksFlow = MutableStateFlow(defaultTasks)

    fun setTasks(tasks: List<TaskEntity>) {
        tasksFlow.value = tasks
    }

    override suspend fun insertTask(task: TaskEntity) {
        tasksFlow.value = tasksFlow.value.filter { it.id != task.id } + task
    }

    override suspend fun updateTask(task: TaskEntity) {
        tasksFlow.value = tasksFlow.value.map {
            if (it.id == task.id) task else it
        }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasksFlow.value = tasksFlow.value.filter { it.id != task.id }
    }

    override fun getTaskById(taskId: Long): TaskEntity? {
        return tasksFlow.value.find { it.id == taskId }
    }

    override fun observeTaskList(): Flow<List<TaskEntity>> = tasksFlow
}