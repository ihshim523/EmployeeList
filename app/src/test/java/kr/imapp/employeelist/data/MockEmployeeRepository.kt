package kr.imapp.employeelist.data

import kr.imapp.employeelist.util.ApiResult

class MockEmployeeRepository : EmployeeRepository {

    var employeeList: ApiResult<List<Employee>> = ApiResult.Success(emptyList())

    override suspend fun getEmployeeList(): ApiResult<List<Employee>> = employeeList
}
