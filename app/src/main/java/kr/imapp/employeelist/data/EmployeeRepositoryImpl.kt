package kr.imapp.employeelist.data

import kr.imapp.employeelist.util.ApiResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmployeeRepositoryImpl(
    private val employeeApi: EmployeeApi
) : EmployeeRepository {

    override suspend fun getEmployeeList(): ApiResult<List<Employee>> {
        return try {
            val list = employeeApi.listEmployee()
            ApiResult.Success(list)
        } catch(e: Exception) {
            ApiResult.Error(e)
        }
    }
}
