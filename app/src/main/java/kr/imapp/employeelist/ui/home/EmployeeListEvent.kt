package kr.imapp.employeelist.ui.home

sealed class EmployeeListEvent {
    data class OnSomeEvent(val someData: String) : EmployeeListEvent()
}
