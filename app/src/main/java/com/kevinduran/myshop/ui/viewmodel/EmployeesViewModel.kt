package com.kevinduran.myshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.models.EmployeePermissions
import com.kevinduran.myshop.domain.usecases.employee.EmployeeUseCases
import com.kevinduran.myshop.ui.states.EmployeesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val employeeUseCases: EmployeeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeesState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { getAll() }
    }

    suspend fun getAll() {
        _uiState.update { it.copy(loading = true) }
        updateItems()
        _uiState.update { it.copy(loading = false) }
    }

    private suspend fun updateItems() {
        val result = employeeUseCases.getAll()
        result.onSuccess {
            _uiState.update { it.copy(allItems = result.getOrElse { emptyList() }) }
        }
        result.onFailure { error ->
            _uiState.update { it.copy(error = "${error.message}") }
        }
    }

    suspend fun addEmployee(name: String, user: String, permissions: EmployeePermissions) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val result = employeeUseCases.addEmployee(name, user, permissions)
            result.onSuccess {
                updateItems()
                _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
            }
            result.handleResult()
        }
    }

    suspend fun deleteAll(entities: List<Employee>) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val result = employeeUseCases.deleteEmployees(entities)
            result.onSuccess {
                updateItems()
                _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
            }
            result.handleResult()
        }
    }

    suspend fun update(
        name: String,
        user: String,
        permissions: EmployeePermissions,
        entity: Employee
    ) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val result = employeeUseCases.updateEmployee(entity, name, user, permissions)
            result.onSuccess {
                updateItems()
                _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
            }
            result.handleResult()
        }
    }

    suspend fun toggleUserAccess(entity: Employee) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val result = employeeUseCases.toggleEmployeeAccess(entity)
            result.onSuccess {
                updateItems()
                _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
            }
            result.handleResult()
        }
    }

    fun toggleSelectedItems(entity: Employee) {
        val items = _uiState.value.selectedItems.toMutableList()
        if (items.contains(entity)) {
            items.remove(entity)
        } else {
            items.add(entity)
        }
        _uiState.update { it.copy(selectedItems = items) }
    }

    private fun Result<Unit>.handleResult() {
        onFailure { error ->
            _uiState.update { it.copy(error = "${error.message}") }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = "", loading = false) }
    }

}