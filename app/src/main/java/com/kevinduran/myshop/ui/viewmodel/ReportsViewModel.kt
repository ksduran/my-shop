package com.kevinduran.myshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.domain.models.Report
import com.kevinduran.myshop.domain.repositories.ReportsRepository
import com.kevinduran.myshop.domain.usecases.report.ReportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: ReportsRepository,
    private val reportUseCases: ReportUseCases
) : ViewModel() {

    private var _allItems = MutableStateFlow<List<Report>>(emptyList())
    val allItems : StateFlow<List<Report>> = _allItems

    private var _selectedItems = MutableStateFlow<List<Report>>(emptyList())
    val selectedItems : StateFlow<List<Report>> = _selectedItems

    init {
        collectReports()
    }

    private fun collectReports() {
        viewModelScope.launch {
            repository.get().collect { value ->
                _allItems.update { value }
            }
        }
    }

    fun addReport(name: String) {
        viewModelScope.launch {
            reportUseCases.addReport(name)
            _selectedItems.update { emptyList() }
        }
    }

    fun deleteReport(entity: Report) {
        viewModelScope.launch {
            reportUseCases.deleteReport(entity)
            _selectedItems.update { emptyList() }
        }
    }

    fun updateReport(name: String, entity: Report) {
        viewModelScope.launch {
            reportUseCases.updateReport(name, entity)
            _selectedItems.update { emptyList() }
        }
    }

    fun reportExists(name: String) : Boolean {
        val validate = allItems.value.firstOrNull { it.name.lowercase() == name.lowercase().trim() }
        return validate != null
    }

}