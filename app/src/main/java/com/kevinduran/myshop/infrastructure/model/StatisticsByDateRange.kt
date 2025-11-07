package com.kevinduran.myshop.infrastructure.model

data class StatisticsByDateRange(
    val totalRecorded: Int = 0,
    val totalPending: Int = 0,
    val totalReturn: Int = 0,
    val totalChanges: Int = 0,
    val totalPaidByTransfer: Int = 0,
    val totalPaidByCash: Int = 0,
    val totalByRaised: Int = 0,
    val totalSold: Int = 0
)