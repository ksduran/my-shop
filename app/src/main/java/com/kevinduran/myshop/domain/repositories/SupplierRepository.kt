package com.kevinduran.myshop.domain.repositories

import com.kevinduran.myshop.domain.models.Supplier

interface SupplierRepository {
    suspend fun add(entity: Supplier): Result<Unit>
    suspend fun add(suppliers: List<Supplier>): Result<Unit>
    suspend fun delete(entity: Supplier): Result<Unit>
    suspend fun delete(suppliers: List<Supplier>): Result<Unit>
    suspend fun get(): Result<List<Supplier>>
    suspend fun update(entity: Supplier): Result<Unit>
    suspend fun update(suppliers: List<Supplier>): Result<Unit>
}
