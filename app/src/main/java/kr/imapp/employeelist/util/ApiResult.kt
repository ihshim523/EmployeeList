package kr.imapp.employeelist.util

import java.lang.Exception

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error<out T>(val exception: Exception) : ApiResult<T>()
}
