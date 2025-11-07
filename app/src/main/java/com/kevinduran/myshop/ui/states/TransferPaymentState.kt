package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.domain.models.TransferPayment

data class TransferPaymentState(
    val license: String = "0",
    val loading: Boolean = false,
    val error: String = "",
    val selectedItems: List<TransferPayment> = emptyList()
)