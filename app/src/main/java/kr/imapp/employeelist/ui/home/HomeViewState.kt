package kr.imapp.employeelist.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.imapp.employeelist.data.Employee

sealed class HomeViewState : Parcelable {

    @Parcelize
    object Loading : HomeViewState()

    @Parcelize
    data class Success(
        val list: List<Employee> = emptyList()
    ) : HomeViewState()

    @Parcelize
    data class Error(
        val exception: Exception
    ) : HomeViewState()
}
