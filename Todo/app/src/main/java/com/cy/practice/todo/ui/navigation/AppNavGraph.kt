package com.cy.practice.todo.ui.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cy.practice.todo.ui.screen.task_form.AddTaskViewModel
import com.cy.practice.todo.ui.screen.task_form.EditTaskViewModel
import com.cy.practice.todo.ui.screen.task_form.TaskFormScreen
import com.cy.practice.todo.ui.screen.task_list.TaskListScreen
import timber.log.Timber

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TaskList
    ) {
        composable<Routes.TaskList> {
            TaskListScreen(
                onAddTask = {
                    navController.navigate(Routes.AddTask)
                },
                onEditTask = {
                    navController.navigate(Routes.EditTask(taskId = it))
                }
            )
        }

        composable<Routes.AddTask> {
            val viewModel: AddTaskViewModel = hiltViewModel()
            TaskFormScreen(
                onSaved = { navController.navigateUp() },
                vm = viewModel
            )
        }

        composable<Routes.EditTask> { backStackEntry ->
            val route: Routes.EditTask = backStackEntry.toRoute()
            Timber.d("Routes.EditTodo:id=${route.taskId}")
            val viewModel: EditTaskViewModel = hiltViewModel()
            TaskFormScreen(
                onSaved = { navController.navigateUp() },
                vm = viewModel
            )
        }
    }

}