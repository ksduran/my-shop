package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.SuppliersDatasource
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.repositories.SupplierRepository
import javax.inject.Inject

class SuppliersRepositoryImpl @Inject constructor(private val datasource: SuppliersDatasource) : SupplierRepository {
    override suspend fun add(entity: Supplier): Result<Unit> {
        return datasource.add(entity)
    }

    override suspend fun add(suppliers: List<Supplier>): Result<Unit> {
        return datasource.add(suppliers)
    }

    override suspend fun delete(entity: Supplier): Result<Unit> {
        return datasource.delete(entity)
    }

    override suspend fun delete(suppliers: List<Supplier>): Result<Unit> {
        return datasource.delete(suppliers)
    }

    override suspend fun get(): Result<List<Supplier>> {
        return datasource.get()
    }

    override suspend fun update(entity: Supplier): Result<Unit> {
        return datasource.update(entity)
    }

    override suspend fun update(suppliers: List<Supplier>): Result<Unit> {
        return datasource.update(suppliers)
    }
}
