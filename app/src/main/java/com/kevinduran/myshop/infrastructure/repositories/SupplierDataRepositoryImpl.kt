package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.SupplierDataDatasource
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.domain.repositories.SupplierDataRepository
import javax.inject.Inject

class SupplierDataRepositoryImpl @Inject constructor(
    private val datasource: SupplierDataDatasource
) : SupplierDataRepository {
    override suspend fun add(entity: SupplierData): Result<Unit> {
        return datasource.add(entity)
    }

    override suspend fun add(supplierData: List<SupplierData>): Result<Unit> {
        return datasource.add(supplierData)
    }

    override suspend fun delete(entity: SupplierData): Result<Unit> {
        return datasource.delete(entity)
    }

    override suspend fun delete(supplierData: List<SupplierData>): Result<Unit> {
        return datasource.delete(supplierData)
    }

    override suspend fun get(supplierId: String): Result<List<SupplierData>> {
        return datasource.get(supplierId)
    }

    override suspend fun update(entity: SupplierData): Result<Unit> {
        return datasource.update(entity)
    }

    override suspend fun update(supplierData: List<SupplierData>): Result<Unit> {
        return datasource.update(supplierData)
    }
}
