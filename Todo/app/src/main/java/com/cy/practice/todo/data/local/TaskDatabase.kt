package com.cy.practice.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        private const val DB_NAME = "task_db"

        fun getInstance(context: Context) =
            Room.databaseBuilder(context, TaskDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration(true)
                .build()
    }
}