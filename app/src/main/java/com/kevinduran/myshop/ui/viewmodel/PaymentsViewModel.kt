package com.kevinduran.myshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.domain.models.TransferPayment
import com.kevinduran.myshop.domain.repositories.TransferPaymentsRepository
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.ui.states.TransferPaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val repository: TransferPaymentsRepository,
    private val preferences: UserPreferencesRepository
) : ViewModel() {

    private val _payments = MutableStateFlow<List<TransferPayment>>(emptyList())
    val payments: StateFlow<List<TransferPayment>> = _payments

    private val _uiState = MutableStateFlow(TransferPaymentState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val license = preferences.getLicenseId().first().toString()
            _uiState.update { it.copy(license = license) }
            getAll()
        }
    }

    suspend fun getAll() {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            _payments.value = repository.getAll().getOrElse { error ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = "${error.message}"
                    )
                }
                emptyList()
            }
            _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
        }
    }

    fun toggleSelectedItems(entity: TransferPayment) {
        val list = uiState.value.selectedItems.toMutableList()
        if (list.contains(entity)) {
            list.remove(entity)
        } else {
            list.add(entity)
        }
        _uiState.update { it.copy(selectedItems = list) }
    }

    suspend fun deleteAll(entities: List<TransferPayment>) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            repository.delete(entities)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = "",
                            selectedItems = emptyList()
                        )
                    }
                    getAll()
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = "${error.message}",
                        )
                    }
                }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = "") }
    }
}

