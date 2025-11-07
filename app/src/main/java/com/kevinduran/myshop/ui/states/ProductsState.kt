package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.domain.models.Product

data class ProductsState(
    val loading: Boolean = false,
    val error: String = "",
    val selectedItems: List<Product> = emptyList(),
    val license: String = ""
)