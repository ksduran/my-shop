package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.domain.models.Employee

data class EmployeesState(
    val loading: Boolean = false,
    val error: String = "",
    val selectedItems: List<Employee> = emptyList(),
    val allItems: List<Employee> = emptyList()
)