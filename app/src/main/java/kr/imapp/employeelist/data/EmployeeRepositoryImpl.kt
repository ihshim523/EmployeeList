package kr.imapp.employeelist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.imapp.employeelist.util.ApiResult

class EmployeeRepositoryImpl(
    private val employeeApi: EmployeeApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : EmployeeRepository {

    override suspend fun getEmployeeList(): ApiResult<List<Employee>> = withContext(dispatcher) {
        try {
            val list = employeeApi.listEmployee().employees

            val malformed = list.any {
                it.uuid.isNullOrEmpty() || it.fullName.isNullOrEmpty() || it.team.isNullOrEmpty()
            }

            if (!malformed) ApiResult.Success(list) else ApiResult.Error(Exception("Malformed JSON"))
        } catch(e: Exception) {
            ApiResult.Error(e)
        }
    }
}
