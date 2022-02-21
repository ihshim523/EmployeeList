package kr.imapp.employeelist.data

import retrofit2.Call
import retrofit2.http.GET

interface EmployeeApi {
    @GET("/sq-mobile-interview/employees_empty.json")
    suspend fun listEmployee(): Employees

    // /sq-mobile-interview/employees.json
    // /sq-mobile-interview/employees_malformed.json
    // /sq-mobile-interview/employees_empty.json
}
