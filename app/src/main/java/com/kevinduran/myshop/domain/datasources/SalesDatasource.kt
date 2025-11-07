package com.kevinduran.myshop.domain.datasources

import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.infrastructure.model.StatisticsByDateRange

interface SalesDatasource {

    suspend fun add(entity: Sale): Result<Unit>
    suspend fun addAll(sales: List<Sale>): Result<Unit>

    suspend fun delete(entity: Sale): Result<Unit>

    suspend fun deleteAll(entities: List<Sale>): Result<Unit>

    suspend fun getToday(
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): Result<List<Sale>>

    suspend fun getEarningsSummary(start: Long, end: Long): Result<EarningsSummary>

    suspend fun getByDateRange(
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): Result<List<Sale>>

    suspend fun getBySupplier(
        supplierId: String,
        from: Long,
        to: Long
    ): Result<List<Sale>>

    suspend fun getStatisticsByDateRange(
        productId: String,
        from: Long,
        to: Long
    ): Result<StatisticsByDateRange>

    suspend fun update(entity: Sale): Result<Unit>

    suspend fun updatedAll(entities: List<Sale>): Result<Unit>

    suspend fun getTotalCashByUser(raisedBy: String): Result<Int>

}