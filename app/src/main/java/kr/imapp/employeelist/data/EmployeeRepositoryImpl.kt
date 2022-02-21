package kr.imapp.employeelist.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.imapp.employeelist.util.ApiResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmployeeRepositoryImpl(
    private val employeeApi: EmployeeApi
) : EmployeeRepository {

    override suspend fun getEmployeeList(): ApiResult<List<Employee>> = withContext(Dispatchers.IO) {
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
