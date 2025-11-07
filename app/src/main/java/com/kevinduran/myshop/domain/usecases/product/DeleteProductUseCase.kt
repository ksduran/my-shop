package com.kevinduran.myshop.domain.usecases.product

import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.repositories.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(entity: Product): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                repository.delete(entity)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend operator fun invoke(products: List<Product>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                repository.delete(products)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
