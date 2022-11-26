package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource

object ServiceLocator
{
    private var database: ToDoDatabase? = null

    @Volatile
    var tasksRepository: TasksRepository? = null

    fun providedTaskRepository(context: Context): TasksRepository
    {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository
    {
        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTasksLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTasksLocalDataSource(context: Context): TasksDataSource
    {
        val database: ToDoDatabase = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase
    {
        val result = Room.databaseBuilder(context.applicationContext, ToDoDatabase::class.java, "Tasks.db").build()
        database = result
        return result
    }
}