package com.kevinduran.myshop.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.domain.repositories.SupplierDataRepository
import com.kevinduran.myshop.domain.usecases.supplierdata.SupplierDataUseCases
import com.kevinduran.myshop.config.enums.SupplierDataType
import com.kevinduran.myshop.ui.states.SupplierDataState
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
class SupplierDataViewModel @Inject constructor(
    private val repository: SupplierDataRepository,
    private val supplierDataUseCases: SupplierDataUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupplierDataState())
    val uiState: StateFlow<SupplierDataState> = _uiState.asStateFlow()

    fun loadSupplierData(supplierId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.get(supplierId)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            items = data,
                            currentDebt = calculateDebt(data),
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = "Error al cargar datos del proveedor: ${error.message}",
                            isLoading = false
                        )
                    }
                    Log.e("SupplierDataViewModel", "Error loading supplier data", error)
                }
        }
    }

    suspend fun add(
        supplierId: String,
        productId: String,
        title: String,
        quantity: Int,
        image: String,
        purchasePrice: Int,
        createdAt: Long,
        type: SupplierDataType = SupplierDataType.INCOME
    ): Boolean {
        _uiState.update { it.copy(isLoading = true) }
        return try {
            withContext(Dispatchers.IO) {
                supplierDataUseCases.addSupplierData(
                    supplierId,
                    productId,
                    title,
                    quantity,
                    image,
                    purchasePrice,
                    createdAt,
                    type.name,
                )
            }
            _uiState.update { it.copy(selectedItems = emptyList()) }
            loadSupplierData(supplierId)
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error al agregar dato: ${e.message}", isLoading = false) }
            Log.e("SupplierDataViewModel", "Error adding supplier data", e)
            false
        }
    }

    suspend fun deleteAll(entities: List<SupplierData>, supplierId: String): Boolean {
        _uiState.update { it.copy(isLoading = true) }
        return try {
            withContext(Dispatchers.IO) {
                supplierDataUseCases.deleteSupplierData(entities)
            }
            _uiState.update { it.copy(selectedItems = emptyList()) }
            loadSupplierData(supplierId)
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error al eliminar datos: ${e.message}", isLoading = false) }
            Log.e("SupplierDataViewModel", "Error deleting supplier data", e)
            false
        }
    }

    suspend fun update(title: String, quantity: Int, purchasePrice: Int, entity: SupplierData): Boolean {
        _uiState.update { it.copy(isLoading = true) }
        return try {
            withContext(Dispatchers.IO) {
                supplierDataUseCases.updateSupplierData(title, quantity, purchasePrice, entity)
            }
            _uiState.update { it.copy(selectedItems = emptyList()) }
            loadSupplierData(entity.supplierId)
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error al actualizar dato: ${e.message}", isLoading = false) }
            Log.e("SupplierDataViewModel", "Error updating supplier data", e)
            false
        }
    }

    fun toggleSelectedItems(entity: SupplierData) {
        _uiState.update { currentState ->
            val selected = currentState.selectedItems
            if (selected.contains(entity)) {
                currentState.copy(selectedItems = selected - entity)
            } else {
                currentState.copy(selectedItems = selected + entity)
            }
        }
    }

    private fun calculateDebt(entities: List<SupplierData>): Int {
        return entities.sumOf { item ->
            when(item.type) {
                SupplierDataType.CAPITAL.name -> item.purchasePrice
                SupplierDataType.INCOME.name -> item.purchasePrice * item.quantity
                SupplierDataType.RETURN.name -> -item.purchasePrice * item.quantity
                SupplierDataType.DEBT_PAYMENT.name -> -item.purchasePrice
                else -> 0
            }
        }
    }
    
    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}
