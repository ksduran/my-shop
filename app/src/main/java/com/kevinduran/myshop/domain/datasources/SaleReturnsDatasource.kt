package com.kevinduran.myshop.domain.datasources

import com.kevinduran.myshop.domain.models.SaleReturn

interface SaleReturnsDatasource {
    suspend fun add(entity: SaleReturn): Result<Unit>
    suspend fun add(returns: List<SaleReturn>): Result<Unit>
    suspend fun delete(entity: SaleReturn): Result<Unit>
    suspend fun delete(returns: List<SaleReturn>): Result<Unit>
    suspend fun getAll(): Result<List<SaleReturn>>
    suspend fun update(entity: SaleReturn): Result<Unit>
}