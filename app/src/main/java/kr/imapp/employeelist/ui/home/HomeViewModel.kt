package kr.imapp.employeelist.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kr.imapp.employeelist.R
import kr.imapp.employeelist.data.Employee
import kr.imapp.employeelist.data.EmployeeRepository
import kr.imapp.employeelist.util.ApiResult
import kr.imapp.employeelist.util.SingleEvent
import kr.imapp.employeelist.util.saveState
import kr.imapp.employeelist.util.setSingleEvent

class HomeViewModel(
    private val employeeRepository: EmployeeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _event = MutableLiveData<SingleEvent<EmployeeListEvent>>()
    val event: LiveData<SingleEvent<EmployeeListEvent>> = _event

    private val _state = MutableStateFlow<HomeViewState>(HomeViewState.Loading)

    val state: LiveData<HomeViewState> = _state
        .saveState(KEY_STATE, savedStateHandle)
        .asLiveData(viewModelScope.coroutineContext)

    private var sortType: SortType
        get() = savedStateHandle.get(KEY_SORT_TYPE) ?: SortType.BY_NAME
        set(value) { savedStateHandle.set(KEY_SORT_TYPE, value) }

    init {
        loadEmployeeList()
    }

    fun onPullToRefresh() {
        loadEmployeeList()
    }

    private fun loadEmployeeList() {
        _state.value = HomeViewState.Loading

        viewModelScope.launch {
            _state.value = when (val result = employeeRepository.getEmployeeList()) {
                is ApiResult.Success -> {
                    if (result.data.isEmpty()) {
                        HomeViewState.Error(errorString = R.string.empty_list_error)
                    } else {
                        val sortedList = sortList(result.data)
                        HomeViewState.Success(
                            sortedList,
                            sortType
                        )
                    }
                }
                is ApiResult.Error -> {
                    HomeViewState.Error(result.exception.localizedMessage)
                }
            }
        }
    }

    fun onSortChanged(type: SortType) {
        sortType = type
        loadEmployeeList()
    }

    private fun sortList(list: List<Employee>): List<Employee> =
        list.sortedBy {
            when (sortType) {
                SortType.BY_NAME -> it.fullName
                SortType.BY_TEAM -> it.team
            }
        }

    companion object {
        private const val KEY_STATE = "keyState"
        private const val KEY_SORT_TYPE = "sortType"
    }
}
