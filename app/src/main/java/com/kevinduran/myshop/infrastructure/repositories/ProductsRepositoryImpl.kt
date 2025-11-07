package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.ProductsDatasource
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.repositories.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val datasource: ProductsDatasource
) : ProductsRepository {
    override suspend fun add(entity: Product) = datasource.add(entity)
    override suspend fun addAll(products: List<Product>) = datasource.addAll(products)
    override suspend fun delete(entity: Product) = datasource.delete(entity)
    override suspend fun delete(products: List<Product>) = datasource.delete(products)
    override suspend fun get() = datasource.get()
    override suspend fun getByUuid(uuid: String) = datasource.getByUuid(uuid)
    override suspend fun update(entity: Product) = datasource.update(entity)
}