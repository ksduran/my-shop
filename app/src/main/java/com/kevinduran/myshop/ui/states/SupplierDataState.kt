package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.domain.models.SupplierData

data class SupplierDataState(
    val items: List<SupplierData> = emptyList(),
    val selectedItems: List<SupplierData> = emptyList(),
    val currentDebt: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
