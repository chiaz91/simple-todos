package com.cy.practice.todo.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cy.practice.todo.ui.screen.task_form.TaskFormScreen
import com.cy.practice.todo.ui.screen.task_list.TaskListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TaskList
    ) {
        composable<Routes.TaskList> {
            TaskListScreen(onAddTask = {
                navController.navigate(Routes.AddTask)
            })
        }

        composable<Routes.AddTask> {
            TaskFormScreen(onSaved = {
                navController.navigateUp()
            })
        }
    }

}