package kr.imapp.employeelist.data

import retrofit2.Call
import retrofit2.http.GET

interface EmployeeApi {
    @GET("/sq-mobile-interview/employees.json")
    fun listEmployee(): List<Employee>
}
