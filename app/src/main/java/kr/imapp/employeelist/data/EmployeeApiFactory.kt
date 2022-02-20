package kr.imapp.employeelist.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EmployeeApiFactory {
    private const val BASE_URL = "https://s3.amazonaws.com"

    private var api: EmployeeApi? = null

    fun create(): EmployeeApi = api ?: run {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(EmployeeApi::class.java)
        return api as EmployeeApi
    }
}
