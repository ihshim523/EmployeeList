package kr.imapp.employeelist.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kr.imapp.employeelist.data.Employee
import kr.imapp.employeelist.data.MockEmployeeRepository
import kr.imapp.employeelist.ui.home.HomeViewModel
import kr.imapp.employeelist.ui.home.HomeViewState
import kr.imapp.employeelist.util.ApiResult
import kr.imapp.employeelist.util.CoroutineDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    private val dispatcher = TestCoroutineDispatcher()
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var coroutineDispatcherRule = CoroutineDispatcherRule(dispatcher)

    private val mockPhotoRepository = MockEmployeeRepository()
    private lateinit var subject: HomeViewModel

    private val savedStateHandle = SavedStateHandle()

    private val employee = Employee(
        uuid = "testUUId",
        fullName = "fullName",
        phoneNumber = "555-555-1234",
        email = "test@email.com",
        biography = "test biography",
        smallPhotoUrl = "smallPhotoUrl",
        largePhotoUrl = "largePhotoUrl",
        team = "test team",
        employeeType = "fullTime",
    )

    @Test
    fun `test view model initialization success`() = dispatcher.runBlockingTest {

        mockPhotoRepository.employeeList = ApiResult.Success(listOf(employee))

        subject = HomeViewModel(mockPhotoRepository, savedStateHandle)

        subject.state.observeForever { state ->
            assertTrue(state is HomeViewState.Success)
            assertEquals(employee, (state as HomeViewState.Success).list.single())
        }
    }

    @Test
    fun `test view model initialization failure`() = dispatcher.runBlockingTest {

        val exception = Exception("test")

        mockPhotoRepository.employeeList = ApiResult.Error(exception)

        subject = HomeViewModel(mockPhotoRepository, savedStateHandle)

        subject.state.observeForever { state ->
            assertTrue(state is HomeViewState.Error)
            assertEquals(exception, (state as HomeViewState.Error).exception)
        }
    }

    @Test
    fun `full to refresh should refresh employee list`() = dispatcher.runBlockingTest {

        mockPhotoRepository.employeeList = ApiResult.Success(listOf(employee))

        subject = HomeViewModel(mockPhotoRepository, savedStateHandle)

        val states = collectStates(subject.state)

        subject.onPullToRefresh()

        assertEquals(employee, (states[0] as HomeViewState.Success).list.single())
        assertTrue(states[1] is HomeViewState.Loading)
        assertEquals(employee, (states[2] as HomeViewState.Success).list.single())
    }

    private fun collectStates(state: LiveData<HomeViewState>): List<HomeViewState> {
        val list = mutableListOf<HomeViewState>()
        state.observeForever {
            list.add(it)
        }
        return list
    }
}
