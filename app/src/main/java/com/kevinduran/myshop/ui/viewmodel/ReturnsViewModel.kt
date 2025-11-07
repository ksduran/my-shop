package com.kevinduran.myshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.domain.models.SaleReturn
import com.kevinduran.myshop.domain.repositories.SaleReturnsRepository
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.ui.states.SaleReturnsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReturnsViewModel @Inject constructor(
    private val repository: SaleReturnsRepository,
    private val preferences: UserPreferencesRepository,
) : ViewModel() {

    private val _returns = MutableStateFlow<List<SaleReturn>>(emptyList())
    val returns: StateFlow<List<SaleReturn>> = _returns.asStateFlow()

    private val _uiState = MutableStateFlow(SaleReturnsState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val license = preferences.getLicenseId().first().toString()
            _uiState.update { it.copy(license = license) }
            loadReturns()
        }
    }

    fun loadReturns() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            repository.getAll()
                .onSuccess { returns ->
                    _returns.value = returns
                    _uiState.update { it.copy(loading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(loading = false, error = error.message) }
                }
        }
    }

    fun toggleSelectedItems(entity: SaleReturn) {
        _uiState.update { currentState ->
            val currentSelected = currentState.selectedItems
            val newSelected = if (currentSelected.contains(entity)) {
                currentSelected - entity
            } else {
                currentSelected + entity
            }
            currentState.copy(selectedItems = newSelected)
        }
    }

    fun selectAll() {
        _uiState.update { currentState ->
            val newSelected = if (currentState.selectedItems.size == _returns.value.size) {
                emptyList()
            } else {
                _returns.value
            }
            currentState.copy(selectedItems = newSelected)
        }
    }

    fun deleteAllSelected() {
        viewModelScope.launch {
            val returnsToDelete = _uiState.value.selectedItems
            if (returnsToDelete.isEmpty()) return@launch

            _uiState.update { it.copy(loading = true, error = null) }
            repository.delete(returnsToDelete)
                .onSuccess {
                    _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
                    loadReturns()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = "Error al eliminar: ${error.message}"
                        )
                    }
                }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
