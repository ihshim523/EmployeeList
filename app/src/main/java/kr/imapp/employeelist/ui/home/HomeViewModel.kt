package kr.imapp.employeelist.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kr.imapp.employeelist.data.EmployeeRepository
import kr.imapp.employeelist.util.ApiResult
import kr.imapp.employeelist.util.SingleEvent
import kr.imapp.employeelist.util.saveState
import kr.imapp.employeelist.util.setSingleEvent

class HomeViewModel(
    private val employeeRepository: EmployeeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _event = MutableLiveData<SingleEvent<EmployeeListEvent>>()
    val event: LiveData<SingleEvent<EmployeeListEvent>> = _event

    private val _state = MutableStateFlow<HomeViewState>(HomeViewState.Loading)

    val state: LiveData<HomeViewState> = _state
        .saveState(KEY_STATE, savedStateHandle)
        .asLiveData(viewModelScope.coroutineContext)

    init {
        loadEmployeeList()
    }

    fun onPullToRefresh() {
        loadEmployeeList()
    }

    private fun loadEmployeeList() {
        _state.value = HomeViewState.Loading

        viewModelScope.launch {
            when (val result = employeeRepository.getEmployeeList()) {
                is ApiResult.Success -> {
                    _state.value = HomeViewState.Success(
                        list = result.data
                    )
                }
                is ApiResult.Error -> {
                    _state.value = HomeViewState.Error(result.exception)
                }
            }
        }
    }

    companion object {
        private const val KEY_STATE = "keyState"
    }
}
