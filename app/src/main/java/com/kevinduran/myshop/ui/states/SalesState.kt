package com.kevinduran.myshop.ui.states

import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.config.helpers.DateRangeGenerator
import com.kevinduran.myshop.domain.models.Sale

data class SalesState(
    val license: String = "0",
    val loading: Boolean = false,
    val error: String = "",
    val selectedItems: List<Sale> = emptyList(),
    val paymentStatus: String = Payment.ByRaised.status,
    val dateRange: Pair<Long, Long> = DateRangeGenerator.today(),
    val searchSales: String = "%",
    val raisedBy: String = "%",
    val order: SalesOrder = SalesOrder.CREATED_AT
)