package com.cy.practice.todo.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var database: TaskDatabase
    private lateinit var taskDao: TaskDao
    private val task1 = TaskEntity(id = 1, name = "Task1", note = "", isDone = false)
    private val task2 = TaskEntity(id = 2, name = "Task2", note = "Task2's note", isDone = false)
    private val task3 = TaskEntity(id = 3, name = "Task3", note = "Task3's note", isDone = true)
    private val taskList = listOf(task1, task2, task3)


    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            TaskDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        taskDao = database.getTaskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = database.close()

    @Test
    fun getTaskById_ifTaskNotExist_thenReturnNull() = runTest {
        val task = taskDao.getTaskById(task1.id)

        assertThat(task).isNull()
    }

    @Test
    fun insertTask_IfTaskIsInserted_ThenItIsStoredInDatabase() = runTest {
        // WHEN
        taskDao.insertTask(task1)

        // ASSERT
        val loadedTask = taskDao.getTaskById(task1.id)
        assertThat(loadedTask).isEqualTo(task1)
    }


    @Test
    fun updateTask_IfTaskExists_ThenItIsUpdatedInDatabase() = runTest {
        // GIVEN
        taskDao.insertTask(task1)

        // WHEN
        val updatedTask = task1.copy(name = "Updated Task", isDone = true)
        taskDao.updateTask(updatedTask)

        // ASSERT
        val loadedTask = taskDao.getTaskById(task1.id)
        assertThat(loadedTask).isEqualTo(updatedTask)
    }


    @Test
    fun deleteTask_IfTaskExists_ThenItIsRemovedFromDatabase() = runTest {
        // GIVEN
        taskDao.insertTask(task1)

        // WHEN
        taskDao.deleteTask(task1)

        // ASSERT
        val loadedTask = taskDao.getTaskById(task1.id)
        assertThat(loadedTask).isNull()
    }

    @Test
    fun observeList_IfNoTasksExist_ThenReturnsEmptyList() = runTest {
        // WHEN
        val loadedList = taskDao.observeTaskList().first()

        // ASSERT
        assertThat(loadedList).isEmpty()
    }

    @Test
    fun observeTaskList_IfTasksExist_ThenReturnsCorrectList() = runTest {
        // GIVEN
        taskList.forEach {
            taskDao.insertTask(it)
        }

        // WHEN
        val loadedList = taskDao.observeTaskList().first()

        // ASSERT
        assertThat(loadedList).isEqualTo(taskList)
    }
}