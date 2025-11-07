package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.SaleReturnsDatasource
import com.kevinduran.myshop.domain.models.SaleReturn
import com.kevinduran.myshop.domain.repositories.SaleReturnsRepository
import javax.inject.Inject

class SaleReturnsRepositoryImpl @Inject constructor(
    private val datasource: SaleReturnsDatasource
) : SaleReturnsRepository {

    override suspend fun add(entity: SaleReturn): Result<Unit> = datasource.add(entity)

    override suspend fun add(returns: List<SaleReturn>): Result<Unit> = datasource.add(returns)

    override suspend fun delete(entity: SaleReturn): Result<Unit> = datasource.delete(entity)

    override suspend fun delete(returns: List<SaleReturn>): Result<Unit> = datasource.delete(returns)

    override suspend fun getAll(): Result<List<SaleReturn>> = datasource.getAll()

    override suspend fun update(entity: SaleReturn): Result<Unit> = datasource.update(entity)
}

