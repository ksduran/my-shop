package com.kevinduran.myshop.domain.repositories

import com.kevinduran.myshop.domain.models.SupplierData

interface SupplierDataRepository {
    suspend fun add(entity: SupplierData): Result<Unit>
    suspend fun add(supplierData: List<SupplierData>): Result<Unit>
    suspend fun delete(entity: SupplierData): Result<Unit>
    suspend fun delete(supplierData: List<SupplierData>): Result<Unit>
    suspend fun get(supplierId: String): Result<List<SupplierData>>
    suspend fun update(entity: SupplierData): Result<Unit>
    suspend fun update(supplierData: List<SupplierData>): Result<Unit>
}
