package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class FakeTestRepository : TasksRepository
{
    private var shouldReturnError = false

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private val observableTask = MutableLiveData<com.example.android.architecture.blueprints.todoapp.data.Result<List<Task>>>()

    fun setReturnError(value: Boolean)
    {
        shouldReturnError = value
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>>
    {
        if (shouldReturnError) {
            return com.example.android.architecture.blueprints.todoapp.data.Result.Error(Exception("Test exception"))
        }
        return com.example.android.architecture.blueprints.todoapp.data.Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task>
    {
        if (shouldReturnError) {
            return com.example.android.architecture.blueprints.todoapp.data.Result.Error(Exception("Test exception"))
        }
        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }
        return com.example.android.architecture.blueprints.todoapp.data.Result.Error(Exception("Could not find task"))
    }

    override suspend fun refreshTasks()
    {
        observableTask.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>>
    {
        runBlockingTest { refreshTasks() }
        return observableTask
    }

    override suspend fun refreshTask(taskId: String)
    {
        refreshTasks()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task)
    {
        tasksServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, true, task.id)
        tasksServiceData[task.id] = completedTask
    }

    override suspend fun completeTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun activateTask(task: Task)
    {
        val activeTask = Task(task.title, task.description, false, task.id)
        tasksServiceData[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks()
    {
        tasksServiceData = tasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteAllTasks()
    {
        tasksServiceData.clear()
        refreshTasks()
    }

    override suspend fun deleteTask(taskId: String)
    {
        tasksServiceData.remove(taskId)
        refreshTasks()
    }

    fun addTasks(vararg tasks: Task)
    {
        for(task in tasks)
        {
            tasksServiceData[task.id] = task
        }

        runBlockingTest {refreshTasks()}
    }
}