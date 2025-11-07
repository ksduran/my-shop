package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.SalesDatasource
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.infrastructure.model.StatisticsByDateRange
import javax.inject.Inject

class SalesRepositoryImpl @Inject constructor(
    private val datasource: SalesDatasource
) : SalesRepository {
    override suspend fun add(entity: Sale): Result<Unit> = datasource.add(entity)
    override suspend fun addAll(sales: List<Sale>): Result<Unit> = datasource.addAll(sales)
    override suspend fun delete(entity: Sale): Result<Unit> = datasource.delete(entity)
    override suspend fun deleteAll(entities: List<Sale>): Result<Unit> =
        datasource.deleteAll(entities)

    override suspend fun getToday(
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): Result<List<Sale>> = datasource.getToday(
        companyName = companyName,
        paymentStatus = paymentStatus,
        raisedBy = raisedBy,
        from = from,
        to = to
    )

    override suspend fun getEarningsSummary(
        start: Long,
        end: Long
    ): Result<EarningsSummary> = datasource.getEarningsSummary(start, end)

    override suspend fun getByDateRange(
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): Result<List<Sale>> = datasource.getByDateRange(
        companyName = companyName,
        paymentStatus = paymentStatus,
        raisedBy = raisedBy,
        from = from,
        to = to
    )

    override suspend fun getBySupplier(
        supplierId: String,
        from: Long,
        to: Long
    ): Result<List<Sale>> = datasource.getBySupplier(
        supplierId = supplierId,
        from = from,
        to = to
    )

    override suspend fun getStatisticsByDateRange(
        productId: String,
        from: Long,
        to: Long
    ): Result<StatisticsByDateRange> = datasource.getStatisticsByDateRange(
        productId = productId,
        from = from,
        to = to
    )

    override suspend fun update(entity: Sale): Result<Unit> = datasource.update(entity)

    override suspend fun updatedAll(entities: List<Sale>): Result<Unit> =
        datasource.updatedAll(entities)

    override suspend fun getTotalCashByUser(raisedBy: String): Result<Int> =
        datasource.getTotalCashByUser(raisedBy)

}