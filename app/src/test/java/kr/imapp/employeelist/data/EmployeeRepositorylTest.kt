package kr.imapp.employeelist.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kr.imapp.employeelist.util.ApiResult
import kr.imapp.employeelist.util.CoroutineDispatcherRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EmployeeRepositorylTest {
    private val dispatcher = TestCoroutineDispatcher()
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var coroutineDispatcherRule = CoroutineDispatcherRule(dispatcher)

    private lateinit var subject: EmployeeRepository
    private val mockEmployeeApi = mockk<EmployeeApi>()

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

    private val employeeMalformed = Employee(
        uuid = "",
        fullName = "",
        phoneNumber = "555-555-1234",
        email = "test@email.com",
        biography = "test biography",
        smallPhotoUrl = "smallPhotoUrl",
        largePhotoUrl = "largePhotoUrl",
        team = "",
        employeeType = "fullTime",
    )

    @Test
    fun `test happy path that returns employee list`() = dispatcher.runBlockingTest {

        coEvery { mockEmployeeApi.listEmployee() } returns Employees(listOf(employee))

        subject = EmployeeRepositoryImpl(
            mockEmployeeApi,
            dispatcher
        )

        val result = subject.getEmployeeList()

        assertTrue(result is ApiResult.Success)
        assertArrayEquals(listOf(employee).toTypedArray(), (result as ApiResult.Success).data.toTypedArray())
    }

    @Test
    fun `test malformed json response returns malformed json error`() = dispatcher.runBlockingTest {

        coEvery { mockEmployeeApi.listEmployee() } returns Employees(listOf(employeeMalformed))

        subject = EmployeeRepositoryImpl(
            mockEmployeeApi,
            dispatcher
        )

        val result = subject.getEmployeeList()

        assertTrue(result is ApiResult.Error)
        assertEquals("Malformed JSON", (result as ApiResult.Error).exception.localizedMessage)
    }

    @Test
    fun `test general exception`() = dispatcher.runBlockingTest {

        coEvery { mockEmployeeApi.listEmployee() } throws Exception("test")

        subject = EmployeeRepositoryImpl(
            mockEmployeeApi,
            dispatcher
        )

        val result = subject.getEmployeeList()

        assertTrue(result is ApiResult.Error)
        assertEquals("test", (result as ApiResult.Error).exception.localizedMessage)
    }
}
