package com.kevinduran.myshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.config.helpers.DateRangeGenerator
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.ui.states.ReportsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsScreenViewModel @Inject constructor(
    private val repository: SalesRepository,
    private val preferences: UserPreferencesRepository
) : ViewModel() {

    private val _sales = MutableStateFlow<List<Sale>>(emptyList())
    val sales: StateFlow<List<Sale>> = _sales

    private val _uiState = MutableStateFlow(ReportsState())
    val uiState = _uiState.asStateFlow()
    private var supplierId: String = ""
    private var dateRange: Pair<Long, Long> = DateRangeGenerator.today()

    fun load(supplier: String) {
        supplierId = supplier
        viewModelScope.launch {
            val license = preferences.getLicenseId().first().toString()
            _uiState.update { it.copy(license = license, loading = true) }
            repository.getBySupplier(
                supplierId = supplierId,
                from = dateRange.first,
                to = dateRange.second
            ).onSuccess { list ->
                _sales.value = list
                _uiState.update { it.copy(loading = false, error = "") }
            }.onFailure { error ->
                _uiState.update { it.copy(loading = false, error = "${error.message}") }
            }
        }
    }

    fun setDateRange(range: Pair<Long, Long>) {
        dateRange = range
        if (supplierId.isNotBlank()) {
            load(supplierId)
        }
        load(supplierId)
    }

    fun dismissError() {
        _uiState.update { it.copy(error = "") }
    }
}
