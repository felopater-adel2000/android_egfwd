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
    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private val observableTask = MutableLiveData<com.example.android.architecture.blueprints.todoapp.data.Result<List<Task>>>()

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>>
    {
        return com.example.android.architecture.blueprints.todoapp.data.Result.Success(tasksServiceData.values.toList())
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

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, true, task.id)
        tasksServiceData[task.id] = completedTask
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
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