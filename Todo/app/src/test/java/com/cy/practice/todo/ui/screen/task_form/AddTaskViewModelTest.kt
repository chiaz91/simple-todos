package com.cy.practice.todo.ui.screen.task_form

import app.cash.turbine.test
import com.cy.practice.todo.MainCoroutineRule
import com.cy.practice.todo.data.repository.FakeTaskRepository
import com.cy.practice.todo.domain.model.Task
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTaskFormViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeRepository: FakeTaskRepository
    private lateinit var viewModel: AddTaskViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        viewModel = AddTaskViewModel(fakeRepository)
    }


    @Test
    fun updateName_ThenUiStateUpdatedWithNewName() = runTest {
        // WHEN
        viewModel.onAction(TaskFormAction.UpdateName("New Task"))

        // ASSERT
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(TaskFormState(name = "New Task"))
        }
    }

    @Test
    fun updateNote_ThenUiStateUpdateWithNewNote() = runTest {
        // WHEN
        viewModel.onAction(TaskFormAction.UpdateNote("New Note"))

        // ASSERT
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(TaskFormState(note = "New Note"))
        }
    }

    @Test
    fun saveTask_WithValidName_ThenSaveTodoAndEmitTodoSavedEvent() = runTest {
        // GIVEN
        viewModel.onAction(TaskFormAction.UpdateName("Valid Task Name"))

        // WHEN
        viewModel.onAction(TaskFormAction.Save)

        // ASSERT
        fakeRepository.observeTaskList().drop(1).test {
            assertThat(awaitItem()).contains(Task(name = "Valid Task Name"))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TaskFormEvent.TaskSaved)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveTask_WithEmptyName_ShouldEmitErrorEvent() = runTest {
        // WHEN
        viewModel.onAction(TaskFormAction.Save)

        // ASSERT
        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TaskFormEvent.Error("Name cannot be empty"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}