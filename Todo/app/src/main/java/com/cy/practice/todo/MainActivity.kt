package com.cy.practice.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cy.practice.todo.ui.screen.task_list.TaskListScreen
import com.cy.practice.todo.ui.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}