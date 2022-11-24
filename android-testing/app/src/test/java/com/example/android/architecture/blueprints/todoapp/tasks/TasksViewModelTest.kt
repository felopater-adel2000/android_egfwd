package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.*
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//when run with AndroidJUnit4 it use Androidx to to choose test method local test or Instrumented
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest
{
    //this line run all background tasks for android before run test
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setupViewModel()
    {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setNewTaskEvent()
    {
        //Given --> a fresh ViewModel
        // some times you need some object like context or application
        //to get this in local test you need AndroidJUnit4 Test dependency
        //val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

//        val observer = Observer<Event<Unit>> {}
//        try {
//            //observe the LiveData forever
//            tasksViewModel.newTaskEvent.observeForever(observer)
//
//            //When --> adding new Task
//            tasksViewModel.addNewTask()
//
//            //Then --> the new Task Event is triggered
//            val value = tasksViewModel.newTaskEvent.value
//            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))
//
//        }finally {
//            tasksViewModel.newTaskEvent.removeObserver(observer)
//        }

        //When --> adding a new task
        tasksViewModel.addNewTask()

        //Then --> the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))

    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible()
    {
        // Given a fresh ViewModel
        //val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))

    }
}