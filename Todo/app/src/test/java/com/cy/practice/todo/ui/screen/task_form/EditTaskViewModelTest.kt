package com.cy.practice.todo.ui.screen.task_form

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.cy.practice.todo.MainCoroutineRule
import com.cy.practice.todo.common.TestDispatcherProvider
import com.cy.practice.todo.data.repository.FakeTaskRepository
import com.cy.practice.todo.domain.model.Task
import com.cy.practice.todo.mockkToRoute
import com.cy.practice.todo.ui.navigation.Routes
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class EditTaskFormViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var fakeTaskRepository: FakeTaskRepository
    private lateinit var viewModel: EditTaskViewModel
    private val dispatcherProvider = TestDispatcherProvider(mainCoroutineRule.testDispatcher)

    val task1 = Task(1, "Task 1", "Task 1's Note", false)
    val initialState = TaskFormState(
        name = task1.name,
        note = task1.note,
        isEditMode = true,
    )

    @Before
    fun setUp() {
        fakeTaskRepository = FakeTaskRepository(listOf(task1))
        savedStateHandle = SavedStateHandle()
        savedStateHandle.mockkToRoute(Routes.EditTask(task1.id))

        viewModel = EditTaskViewModel(
            savedStateHandle,
            fakeTaskRepository,
            dispatcherProvider
        )
    }

    @Test
    fun uiState_loadedTask_thenReflectedOnUiState() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(TaskFormState())
            assertThat(awaitItem()).isEqualTo(initialState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateName_thenUiStateUpdateWithNewName() = runTest {
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(initialState)

            viewModel.onAction(TaskFormAction.UpdateName("New Task Name"))

            assertThat(awaitItem()).isEqualTo(initialState.copy(name = "New Task Name"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateNote_thenUiStateUpdateWithNewNote() = runTest {
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(initialState)

            viewModel.onAction(TaskFormAction.UpdateNote("New Note"))

            assertThat(awaitItem()).isEqualTo(initialState.copy(note = "New Note"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveTask_WithValidForm_ShouldUpdateTaskAndEmitTaskSavedEvent() = runTest {
        // GIVEN
        advanceUntilIdle()
        viewModel.onAction(TaskFormAction.UpdateName("Valid Task Name"))
        viewModel.onAction(TaskFormAction.UpdateNote("Valid Task Note"))

        // WHEN
        viewModel.onAction(TaskFormAction.Save)

        // ASSERT
        fakeTaskRepository.observeTaskList().drop(1).test {
            assertThat(awaitItem()).contains(
                task1.copy(
                    name = "Valid Task Name",
                    note = "Valid Task Note"
                )
            )
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TaskFormEvent.TaskSaved)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun saveTask_WithEmptyTitle_ShouldEmitErrorEvent() = runTest {
        // WHEN
        viewModel.onAction(TaskFormAction.UpdateName(""))
        viewModel.onAction(TaskFormAction.Save)

        // ASSERT
        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TaskFormEvent.Error("Name cannot be empty"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}