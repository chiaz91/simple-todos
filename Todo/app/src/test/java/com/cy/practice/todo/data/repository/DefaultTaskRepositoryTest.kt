package com.cy.practice.todo.data.repository

import com.cy.practice.todo.data.local.FakeTaskDao
import com.cy.practice.todo.data.mapper.toEntityList
import com.cy.practice.todo.domain.model.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTaskRepositoryTest {

    private lateinit var fakeTaskDao: FakeTaskDao
    private lateinit var repository: DefaultTaskRepository
    private val task1 = Task(id = 1, name = "Task1", note = "", isDone = false)
    private val task2 = Task(id = 2, name = "Task2", note = "Task2's note", isDone = false)
    private val task3 = Task(id = 3, name = "Task3", note = "Task3's note", isDone = true)
    private val taskList = listOf(task1, task2, task3)

    @Before
    fun setUp() {
        fakeTaskDao = FakeTaskDao()
        repository = DefaultTaskRepository(fakeTaskDao)
    }

    @Test
    fun getTaskById_ifTaskNotExist_thenReturnNull() = runTest {
        val task = repository.getTaskById(1)

        assertThat(task).isNull()
    }

    @Test
    fun getTaskById_ifTaskExist_thenReturnCorrectTask() = runTest {
        fakeTaskDao.setTasks(taskList.toEntityList())
        val task = repository.getTaskById(task1.id)

        assertThat(task).isEqualTo(task1)
    }

    @Test
    fun addTask_IfTaskIsInserted_ThenItExistsInSource() = runTest {
        // WHEN
        repository.addTask(task1)

        // ASSERT
        val loadedTask = repository.getTaskById(task1.id)
        assertThat(loadedTask).isEqualTo(task1)
    }

    @Test
    fun updateTask_IfTaskExists_ThenItIsUpdatedInSource() = runTest {
        // GIVEN
        fakeTaskDao.setTasks(taskList.toEntityList())

        // WHEN
        val updatedTask = task1.copy(name = "updated task", isDone = true)
        repository.updateTask(updatedTask)

        // ASSERT
        val loadedTask = repository.getTaskById(task1.id)
        assertThat(loadedTask).isEqualTo(updatedTask)
    }

    @Test
    fun deleteTask_IfTaskIsDeleted_ThenItIsRemovedFromSource() = runTest {
        // GIVEN
        fakeTaskDao.setTasks(taskList.toEntityList())
        // WHEN
        repository.deleteTask(task1)
        // ASSERT
        val tasks = repository.getTaskById(task1.id)
        assertThat(tasks).isNull()
    }

    @Test
    fun observeTaskList_IfTasksDidNotExist_ThenReturnsEmptyList() = runTest {
        // WHEN
        val tasks = repository.observeTaskList().first()

        // ASSERT
        assertThat(tasks).isEmpty()
    }

    @Test
    fun observeTaskList_IfTasksExist_ThenReturnsCorrectList() = runTest {
        // GIVEN
        taskList.forEach {
            repository.addTask(it)
        }

        // WHEN
        val tasks = repository.observeTaskList().first()

        // ASSERT
        assertThat(tasks).isEqualTo(taskList)
    }
}