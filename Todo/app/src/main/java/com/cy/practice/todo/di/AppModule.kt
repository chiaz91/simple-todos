package com.cy.practice.todo.di

import android.content.Context
import com.cy.practice.todo.data.local.TaskDatabase
import com.cy.practice.todo.data.repository.DefaultTaskRepository
import com.cy.practice.todo.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return TaskDatabase.getInstance(context)
    }


    @Provides
    @Singleton
    fun provideRepository(taskDatabase: TaskDatabase): TaskRepository {
        return DefaultTaskRepository(taskDatabase.getTaskDao())
    }
}