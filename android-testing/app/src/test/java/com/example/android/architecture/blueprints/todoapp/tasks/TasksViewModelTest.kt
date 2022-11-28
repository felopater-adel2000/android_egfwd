package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.*
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//when run with AndroidJUnit4 it use Androidx to to choose test method local test or Instrumented
//@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TasksViewModelTest
{
    @get:Rule
    val mainCoroutineRult = MainCoroutineRule()

    private lateinit var tasksRepository: FakeTestRepository
    //this line run all background tasks for android before run test
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setupViewModel()
    {
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)
        tasksViewModel = TasksViewModel(tasksRepository)
    }

    /**We do not need After and Before because we use MainCoroutineRule*/
    //    // coroutine scope for testing
//    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
//    @Before
//    fun setDispatcher()
//    {
//        //set main Dispatchers to testDispatcher instead of Android's Main looper
//        Dispatchers.setMain(testDispatcher)
//    }
//    @After
//    fun tearDownDispatcher()
//    {
//        //After finish testing reset and clean all things
//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
//    }

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

    @Test
    fun completeTask_dataAndSnackbarUpdated()
    {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)

        // Mark the task as complete task.
        tasksViewModel.completeTask(task, true)

        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}