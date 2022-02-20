package kr.imapp.employeelist.data

import kr.imapp.employeelist.util.ApiResult

interface EmployeeRepository {

    suspend fun getEmployeeList(): ApiResult<List<Employee>>
}
