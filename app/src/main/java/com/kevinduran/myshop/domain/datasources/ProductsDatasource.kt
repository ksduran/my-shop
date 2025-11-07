package com.kevinduran.myshop.domain.datasources

import com.kevinduran.myshop.domain.models.Product

interface ProductsDatasource {
    suspend fun add(entity: Product): Result<Unit>
    suspend fun addAll(products: List<Product>): Result<Unit>
    suspend fun delete(entity: Product): Result<Unit>
    suspend fun delete(products: List<Product>): Result<Unit>
    suspend fun get(): Result<List<Product>>
    suspend fun getByUuid(uuid: String): Result<Product?>
    suspend fun update(entity: Product): Result<Unit>

}
