package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.domain.models.SaleReturn

data class SaleReturnsState(
    val license: String = "0",
    val loading: Boolean = false,
    val error: String? = null,
    val selectedItems: List<SaleReturn> = emptyList()
)