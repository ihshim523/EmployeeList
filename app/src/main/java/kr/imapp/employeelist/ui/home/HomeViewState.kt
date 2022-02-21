package kr.imapp.employeelist.ui.home

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kr.imapp.employeelist.data.Employee

sealed class HomeViewState : Parcelable {

    @Parcelize
    object Loading : HomeViewState()

    @Parcelize
    data class Success(
        val list: List<Employee> = emptyList(),
        val sortType: SortType,
    ) : HomeViewState()

    @Parcelize
    data class Error(
        val errorMessage: String? = null,
        @StringRes val errorString: Int = 0,
    ) : HomeViewState()
}
