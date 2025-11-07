package com.kevinduran.myshop.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.repositories.SupplierRepository
import com.kevinduran.myshop.domain.usecases.supplier.SupplierUseCases
import com.kevinduran.myshop.ui.states.SuppliersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val repository: SupplierRepository,
    private val supplierUseCases: SupplierUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SuppliersState())
    val uiState: StateFlow<SuppliersState> = _uiState.asStateFlow()

    init {
        loadSuppliers()
    }

    fun loadSuppliers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.get()
                .onSuccess { suppliers ->
                    _uiState.update { it.copy(suppliers = suppliers, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = "Error al cargar proveedores: ${error.message}",
                            isLoading = false
                        )
                    }
                    Log.e("SuppliersViewModel", "Error loading suppliers", error)
                }
        }
    }

    suspend fun addSupplier(name: String, debtControl: Int): Supplier? {
        _uiState.update { it.copy(isLoading = true) }
        return try {
            val newSupplier = withContext(Dispatchers.IO) {
                supplierUseCases.addSupplier(name, debtControl)
            }
            _uiState.update { it.copy(selectedSuppliers = emptyList()) }
            loadSuppliers()
            newSupplier
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    error = "Error al agregar proveedor: ${e.message}",
                    isLoading = false
                )
            }
            Log.e("SuppliersViewModel", "Error adding supplier", e)
            null
        }
    }

    suspend fun deleteAllSuppliers(entities: List<Supplier>) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            withContext(Dispatchers.IO) {
                supplierUseCases.deleteSupplier(entities)
            }
            _uiState.update { it.copy(selectedSuppliers = emptyList()) }
            loadSuppliers()
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    error = "Error al eliminar proveedores: ${e.message}",
                    isLoading = false
                )
            }
            Log.e("SuppliersViewModel", "Error deleting suppliers", e)
        }
    }

    suspend fun updateSupplier(name: String, debtControl: Int, entity: Supplier) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            withContext(Dispatchers.IO) {
                supplierUseCases.updateSupplier(name, debtControl, entity)
            }
            _uiState.update { it.copy(selectedSuppliers = emptyList()) }
            loadSuppliers()
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    error = "Error al actualizar proveedor: ${e.message}",
                    isLoading = false
                )
            }
            Log.e("SuppliersViewModel", "Error updating supplier", e)
        }
    }

    fun toggleSelectedSuppliers(entity: Supplier) {
        _uiState.update { currentState ->
            val selected = currentState.selectedSuppliers
            if (selected.contains(entity)) {
                currentState.copy(selectedSuppliers = selected - entity)
            } else {
                currentState.copy(selectedSuppliers = selected + entity)
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}