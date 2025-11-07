package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.domain.models.Supplier

data class SuppliersState(
    val suppliers: List<Supplier> = emptyList(),
    val selectedSuppliers: List<Supplier> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
